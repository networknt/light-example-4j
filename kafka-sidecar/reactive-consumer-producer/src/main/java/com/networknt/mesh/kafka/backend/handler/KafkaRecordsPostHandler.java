package com.networknt.mesh.kafka.backend.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.body.BodyHandler;
import com.networknt.config.Config;
import com.networknt.config.JsonMapper;
import com.networknt.handler.LightHttpHandler;
import com.networknt.http.HttpStatus;
import com.networknt.http.MediaType;
import com.networknt.kafka.entity.ConsumerRecord;
import com.networknt.kafka.entity.ProduceRecord;
import com.networknt.kafka.entity.ProduceRequest;
import com.networknt.kafka.entity.RecordProcessedResult;
import com.networknt.mesh.kafka.backend.ApplicationConfig;
import com.networknt.utility.Constants;
import com.networknt.utility.ObjectUtils;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * This is the endpoint that will be called by the Kafka-sidecar with reactive consumer enabled. The request will be a batch
 * of Record objects that polled from the Kafka topic(s) from the kafka-sidecar. Within this handler, you can process the
 * batch and return the RecordProcessedResult to the sidecar for auditing and dead letter processing.
 *
 */
public class KafkaRecordsPostHandler implements LightHttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(KafkaRecordsPostHandler.class);
    public static final String GENERIC_EXCEPTION = "ERR10014";
    public static ApplicationConfig config = (ApplicationConfig) Config.getInstance().getJsonObjectConfig(ApplicationConfig.CONFIG_NAME, ApplicationConfig.class);
    ObjectMapper objectMapper = Config.getInstance().getMapper();
    HttpClient httpClient = null;

    public KafkaRecordsPostHandler() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Instant start = Instant.now();
        // for each record, we might process them in separated thread and this results might be written by multiple threads
        // at the same time, make it synchronized so that all write to the backing list will be guaranteed serial access.
        List<RecordProcessedResult> results = Collections.synchronizedList(new ArrayList<>());
        List<CompletableFuture<HttpResponse<String>>> completedProduces = new ArrayList<>();
        List<Map<String, Object>> records = (List<Map<String, Object>>)exchange.getAttachment(BodyHandler.REQUEST_BODY);
        if(logger.isDebugEnabled()) logger.debug("Received payload at time = " + LocalDateTime.now() + " with batch size = " + records.size());
        try {
            records.forEach(recordMap -> {
                ConsumerRecord record = objectMapper.convertValue(recordMap, ConsumerRecord.class);
                String topic = record.getTopic();
                logger.info("topic = " + topic);
                int partition = record.getPartition();
                logger.info("partition = " + partition);
                long offset = record.getOffset();
                logger.info("offset = " + offset);
                Object key = record.getKey();
                logger.info("key = " + key);
                Object value = record.getValue();
                String rawJson = JsonMapper.toJson(value);
                logger.info("value = " + rawJson);
                Map<String, String> headers = record.getHeaders();
                if(headers != null && headers.size() > 0) {
                    headers.entrySet().forEach(entry -> {
                        logger.info("header key = " + entry.getKey() + " header value = " + entry.getValue());
                    });
                }
                // handle the correlation and traceability with logger context per record.
                // the header won't be null and the correlationId must exist.
                String correlationId = headers.get(Constants.CORRELATION_ID_STRING);
                // we know the value is a map in our case, so it is safe to cast it.
                String traceabilityId = getTraceabilityId(headers, (Map<String, Object>)record.getValue());

                try {
                    // transform the key and value here. Let's combine the first and last name into a full name.
                    // we know the value is a map object so let's manipulate it directly.
                    Map<String, String> valueMap = (Map<String, String>)value;
                    String firstName = valueMap.get("firstName");
                    String lastName = valueMap.get("lastName");
                    String fullName = firstName + " " + lastName;
                    valueMap.put("fullName", fullName);

                    MDC.put(Constants.CORRELATION_ID_STRING, correlationId);
                    MDC.put(Constants.TRACEABILITY_ID_STRING, traceabilityId);
                    if(logger.isDebugEnabled()) logger.debug("Start processing of message with key = " + key + " value = " + rawJson);

                    // create a new record and produce to the Kafka if isProduce returns true.
                    if(isProduce(record)) {
                        ProduceRecord produceRecord = new ProduceRecord();
                        ProduceRequest produceRequest = new ProduceRequest();
                        produceRecord.setKey(Optional.of(objectMapper.readTree(objectMapper.writeValueAsString(key))));
                        // send the transformed valueMap with fullName to the transformed.account topic.
                        produceRecord.setValue(Optional.of(objectMapper.convertValue(valueMap, JsonNode.class)));
                        // need to pass the traceabilityId and correlationId from the original record to the new transformed record for tracing.
                        produceRecord.setTraceabilityId(Optional.of(traceabilityId));
                        produceRecord.setCorrelationId(Optional.of(correlationId));
                        produceRequest.setRecords(Arrays.asList(produceRecord));
                        // inject the schema version for the key and value for producer schema cache.
                        produceRequest.setKeySchemaVersion(Optional.of(config.getKeySchemaVersion()));
                        produceRequest.setValueSchemaVersion(Optional.of(config.getValueSchemaVersion()));
                        CompletableFuture<HttpResponse<String>> produceResponse = produceToSidecar(produceRequest, config.getTargetTopicName());
                        CompletableFuture<HttpResponse<String>> completedProduce = produceResponse.whenComplete((response, ex) -> {
                            MDC.put(Constants.TRACEABILITY_ID_STRING, traceabilityId);
                            MDC.put(Constants.CORRELATION_ID_STRING, correlationId);
                            if(response.statusCode() == 200) {
                                if(logger.isDebugEnabled()) logger.debug("Producing successfully completed for " + key);
                                RecordProcessedResult rpr = new RecordProcessedResult(record, true, null, correlationId, traceabilityId, key != null ? key.toString() : null, System.currentTimeMillis());
                                results.add(rpr);
                            } else {
                                if(logger.isDebugEnabled()) logger.debug("Error producing message for key = " + key + " with response = " + response.body());
                                RecordProcessedResult rpr = new RecordProcessedResult(record, false, response.body(), correlationId, traceabilityId, key != null ? key.toString() : null, System.currentTimeMillis());
                                results.add(rpr);
                            }
                            MDC.clear();
                        });
                        completedProduces.add(completedProduce);
                    } else {
                        if(logger.isDebugEnabled()) logger.debug("Skipping produce for message with no key but value = " + rawJson);
                        RecordProcessedResult rpr = new RecordProcessedResult(record, true, null, correlationId, traceabilityId,  key != null ? key.toString() : null, System.currentTimeMillis());
                        results.add(rpr);
                    }
                } catch (Exception e) {
                    RecordProcessedResult rpr = new RecordProcessedResult(record, false, ExceptionUtils.getStackTrace(e), correlationId, traceabilityId, key != null ? key.toString() : null, System.currentTimeMillis());
                    results.add(rpr);
                    logger.error("Exception occurs while processing value = " + rawJson + " with key = " + key, e);
                }
            });
            completedProduces.forEach(CompletableFuture::join);
            Instant produceDone = Instant.now();
            if(logger.isDebugEnabled()) logger.debug("Producing done in " + Duration.between(start, produceDone).toMillis() + " for batch size " + records.size());
            String res = JsonMapper.toJson(results);
            Instant finishedBatch = Instant.now();
            if(logger.isDebugEnabled()) logger.debug("Returning result at " + LocalDateTime.now() + " batch size = " + results.size() + " time taken in Millis = " + Duration.between(start, finishedBatch).toMillis());
            MDC.clear();
            exchange.setStatusCode(HttpStatus.OK.value());
            exchange.getResponseSender().send(res);
        } catch (Exception e) {
            logger.error("Exception occurs while processing the batch:", e);
            MDC.clear();
            setExchangeStatus(exchange, GENERIC_EXCEPTION, e.getMessage());
        }
    }

    private String getTraceabilityId(Map<String, String> headers, Map<String, Object> value) {
        // first check if the traceabilityId is in the header. If not, try to pick a field in
        // the value to start the tracing. The headers must be not empty as it is from the sidecar.
        String traceabilityId = null;
        if(!ObjectUtils.isEmpty(headers.get(Constants.TRACEABILITY_ID_STRING))) {
            traceabilityId =  headers.get(Constants.TRACEABILITY_ID_STRING);
        } else {
            // here we expect the value is a JSON object that is presented as a map and there is
            // a field in the value. You should use any identifier in the business as traceabilityId.
            // For example, accountNo, userId, email etc.
            traceabilityId = (String)value.get("accountNo");
        }
        if(traceabilityId == null) {
            throw new RuntimeException("TraceabilityId cannot be found in headers and value.");
        }
        return traceabilityId;
    }

    private CompletableFuture<HttpResponse<String>> produceToSidecar(ProduceRequest produceRequest, String topicName) throws Exception {
        if(logger.isDebugEnabled()) logger.debug("Producing the transformed message = " + JsonMapper.toJson(produceRequest) + " to sidecar at " + LocalDateTime.now());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(config.getSidecarProducerUrl() + topicName))
                .POST(HttpRequest.BodyPublishers.ofString(JsonMapper.toJson(produceRequest)))
                .setHeader(Headers.CONTENT_TYPE_STRING, MediaType.APPLICATION_JSON.toString())
                .build();
        HttpResponse<String> responseEntity = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if(logger.isDebugEnabled()) logger.debug("Produced transformed message to sidecar at " + LocalDateTime.now());
        return CompletableFuture.completedFuture(responseEntity);
    }

    private boolean isProduce(ConsumerRecord record) {
        // make decision if produce to the target topic based on the original record
        // The following is an example to produce if the original key is not empty.
        if(!ObjectUtils.isEmpty(record.getKey())) {
            return true;
        } else {
            return false;
        }
    }
}
