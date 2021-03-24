package com.networknt.mesh.kafka.backend.handler;

import com.networknt.body.BodyHandler;
import com.networknt.config.JsonMapper;
import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import java.util.List;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class KafkaRecordsPostHandler implements LightHttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        List<String> body = (List<String>)exchange.getAttachment(BodyHandler.REQUEST_BODY);
        logger.info("received call from the sidecar: " + JsonMapper.toJson(body));
        List<Map<String, Object>> records = JsonMapper.
        exchange.setStatusCode(201);
        exchange.endExchange();
    }
}
