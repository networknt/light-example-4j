package com.networknt.petstore.handler;

import com.networknt.body.BodyHandler;
import com.networknt.config.Config;
import com.networknt.http.*;
import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import com.networknt.petstore.model.Pet;
import io.undertow.util.Headers;

import java.util.Deque;
import java.util.Map;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class PetsPostHandler implements LightHttpHandler {

    public PetsPostHandler () {
    }


    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = "{}";
        exchange.setStatusCode(HttpStatus.OK.value());
        exchange.getResponseSender().send(body);
    }
}
