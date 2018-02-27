package com.networknt.webserver.handler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.awt.*;
import java.nio.ByteBuffer;

public class TextHandler implements HttpHandler {

    private static final ByteBuffer buffer;
    private static final String MESSAGE = "Hello World";

    static {
        buffer = ByteBuffer.allocateDirect(MESSAGE.length());
        try {
            buffer.put(MESSAGE.getBytes("US-ASCII"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        buffer.flip();
    }

    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().put(
                Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(buffer.duplicate());
    }
}
