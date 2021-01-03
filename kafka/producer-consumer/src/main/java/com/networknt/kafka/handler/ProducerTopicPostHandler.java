package com.networknt.kafka.handler;

import com.networknt.body.BodyHandler;
import com.networknt.handler.LightHttpHandler;
import com.networknt.kafka.producer.LightProducer;
import com.networknt.service.SingletonServiceFactory;
import io.undertow.server.HttpServerExchange;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class ProducerTopicPostHandler implements LightHttpHandler {
    private static Logger logger = LoggerFactory.getLogger(ProducerTopicPostHandler.class);
    private static String STATUS_ACCEPTED = "SUC10202";
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        logger.debug("ProducerTopicPostHandler start");
        String topic = exchange.getQueryParameters().get("topic").getFirst();
        logger.info("topic: " + topic);
        List<Map<String, Object>> list = (List)exchange.getAttachment(BodyHandler.REQUEST_BODY);

        LightProducer producer = SingletonServiceFactory.getBean(LightProducer.class);
        BlockingQueue<ProducerRecord<byte[], byte[]>> txQueue = producer.getTxQueue();

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> itemMap = list.get(i);
            String key = (String)itemMap.get("key");
            String value = (String)itemMap.get("value");
            ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(topic, key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8));
            try {
                txQueue.put(record);
            } catch (InterruptedException e) {
                logger.error("Exception:", e);
            }
            logger.debug("Send message with key = " + key + " value = " + value  + " to topic: " + topic);
        }
        setExchangeStatus(exchange, STATUS_ACCEPTED);
    }
}
