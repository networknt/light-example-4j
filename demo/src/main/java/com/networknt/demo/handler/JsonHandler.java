package com.networknt.demo.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Objects;

public class JsonHandler implements HttpHandler {
    private final ObjectMapper objectMapper;

    public JsonHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().put(
                Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send(ByteBuffer.wrap(
                objectMapper.writeValueAsBytes(
                        Collections.singletonMap("message", "Hello World"))));
    }
}
