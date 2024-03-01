package com.networknt.petstore.handler;

import com.networknt.config.Config;
import com.networknt.http.MediaType;
import com.networknt.handler.LightHttpHandler;
import com.networknt.http.HttpMethod;
import com.networknt.http.RequestEntity;
import com.networknt.http.ResponseEntity;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import org.xnio.IoUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.Map;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class PetsPetIdGetHandler implements LightHttpHandler {
    public PetsPetIdGetHandler () {
    }


    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        InputStream inputStream = Config.getInstance().getInputStreamFromFile("big.json");
        String text = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        exchange.setStatusCode(200);
        exchange.getResponseSender().send(text);
    }
}
