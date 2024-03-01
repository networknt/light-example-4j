package com.networknt.petstore.handler;

import com.networknt.body.BodyHandler;
import com.networknt.config.Config;

import com.networknt.handler.LightHttpHandler;
import com.networknt.http.HttpMethod;
import com.networknt.http.HttpStatus;
import com.networknt.http.MediaType;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HeaderMap;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.Map;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class StreamsGetHandler implements LightHttpHandler {


    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.setStatusCode(200);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/stream+json");
        exchange.getResponseHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
        InputStream inputStream = Config.getInstance().getInputStreamFromFile("big.json");
        String text = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        exchange.getResponseSender().send(text);
    }
}
