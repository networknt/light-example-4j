package com.networknt.petstore.handler;

import com.networknt.config.Config;
import com.networknt.petstore.service.PetsPetIdGetService;
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
    PetsPetIdGetService service;

    public PetsPetIdGetHandler () {
        this.service = new PetsPetIdGetService ();
    }

    
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        HeaderMap requestHeaders = exchange.getRequestHeaders();
        Map<String, Deque<String>> queryParameters = exchange.getQueryParameters();
        Map<String, Deque<String>> pathParameters = exchange.getPathParameters();
        HttpMethod httpMethod = HttpMethod.resolve(exchange.getRequestMethod().toString());
        exchange.setStatusCode(200);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        InputStream inputStream = Config.getInstance().getInputStreamFromFile("big.json");
        String text = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        exchange.getResponseSender().send(text);
    }
}
