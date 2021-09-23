package com.networknt.mesh.kafka.backend.handler;

import com.networknt.body.BodyHandler;
import com.networknt.config.JsonMapper;
import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.util.Map;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class KsqlDBRowsPostHandler implements LightHttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        Map body = (Map) exchange.getAttachment(BodyHandler.REQUEST_BODY);
        logger.info("received call from the sidecar: " + JsonMapper.toJson(body));
        exchange.setStatusCode(201);
        exchange.endExchange();
    }
}
