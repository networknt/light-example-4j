package com.networknt.mesh.kafka.backend.handler;

import com.networknt.body.BodyHandler;
import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import java.util.List;
import java.util.Map;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class KafkaRecordsPostHandler implements LightHttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        List<Map<String, Object>> records = (List<Map<String, Object>>)exchange.getAttachment(BodyHandler.REQUEST_BODY);
        for(int i = 0; i < records.size(); i++) {
            Map<String, Object> record = records.get(i);
            logger.debug("topic = " + record.get("topic"));
            logger.debug("partition = " + record.get("partition"));
            logger.debug("offset = " + record.get("offset"));
            logger.debug("key = " + record.get("key"));
            logger.debug("value = " + record.get("value"));
            Map<String, String> headerMap = (Map<String, String>)record.get("headers");
            headerMap.entrySet().forEach(entry -> {
                logger.debug("header key = " + entry.getKey() + " header value = " + entry.getValue());
            });
        }
        exchange.setStatusCode(201);
        exchange.endExchange();
    }
}
