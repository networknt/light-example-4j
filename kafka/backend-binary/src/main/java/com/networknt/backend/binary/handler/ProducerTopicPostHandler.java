package com.networknt.backend.binary.handler;

import com.networknt.body.BodyHandler;
import com.networknt.config.Config;
import com.networknt.backend.binary.service.ProducerTopicPostService;
import com.networknt.config.JsonMapper;
import com.networknt.handler.LightHttpHandler;
import com.networknt.http.HttpMethod;
import com.networknt.http.RequestEntity;
import com.networknt.http.ResponseEntity;
import com.networknt.kafka.entity.ConsumerRecord;
import com.networknt.kafka.entity.RecordProcessedResult;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class ProducerTopicPostHandler implements LightHttpHandler {
    ProducerTopicPostService service;

    public ProducerTopicPostHandler () {
        this.service = new ProducerTopicPostService ();
    }

    /**
     * We expect the record has a key of string and value of string. This handle will encode the value with base64 and then
     * send to the Kafka sidecar to produce to the Kafka topic.
     *
     * @param exchange
     * @throws Exception
     */
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        List<RecordProcessedResult> results = new ArrayList<>();
        List<Map<String, Object>> records = (List<Map<String, Object>>)exchange.getAttachment(BodyHandler.REQUEST_BODY);
        for(int i = 0; i < records.size(); i++) {
            ConsumerRecord record = Config.getInstance().getMapper().convertValue(records.get(i), ConsumerRecord.class);
            logger.info("topic = " + record.getTopic());
            logger.info("partition = " + record.getPartition());
            logger.info("offset = " + record.getOffset());
            logger.info("key = " + record.getKey());
            logger.info("value = " + record.getValue());
            Map<String, String> headerMap = record.getHeaders();
            if(headerMap != null && headerMap.size() > 0) {
                headerMap.entrySet().forEach(entry -> {
                    logger.info("header key = " + entry.getKey() + " header value = " + entry.getValue());
                });
            }

            if(i == 1) {
                // simulate runtime exception that causes record cannot be processed successfully and ask
                // sidecar to put the record to the dead letter queue.
                try {
                    int j = 10 / 0;
                } catch (RuntimeException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    RecordProcessedResult rpr = new RecordProcessedResult(record, false, sw.toString());
                    results.add(rpr);
                }
            } else {
                // simulate processed successfully.
                RecordProcessedResult rpr = new RecordProcessedResult(record, true, null);
                results.add(rpr);
            }
        }
        exchange.setStatusCode(200);
        exchange.getResponseSender().send(JsonMapper.toJson(results));
    }
}
