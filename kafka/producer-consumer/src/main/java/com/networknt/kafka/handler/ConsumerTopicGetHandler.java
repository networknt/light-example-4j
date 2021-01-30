package com.networknt.kafka.handler;

import com.networknt.config.JsonMapper;
import com.networknt.handler.LightHttpHandler;
import com.networknt.httpstring.ContentType;
import com.networknt.kafka.ConsumerStartupHook;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumerTopicGetHandler implements LightHttpHandler {
    private static Logger logger = LoggerFactory.getLogger(ConsumerTopicGetHandler.class);

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        logger.debug("ConsumerTopicGetHandler start");
        String topic = exchange.getQueryParameters().get("topic").getFirst();
        logger.info("topic: " + topic);
        ConsumerStartupHook.consumer.subscribe(topic);
        List<Map<String, Object>> list = ConsumerStartupHook.consumer.poll();
        if(list != null) {
            exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "application/json");
            exchange.getResponseSender().send(JsonMapper.toJson(list));
        }
        exchange.endExchange();
    }
}
