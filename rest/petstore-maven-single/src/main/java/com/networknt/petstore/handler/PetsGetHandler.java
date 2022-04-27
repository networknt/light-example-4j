package com.networknt.petstore.handler;

import com.networknt.body.BodyHandler;
import com.networknt.config.Config;
import com.networknt.petstore.service.PetsGetService;
import com.networknt.handler.LightHttpHandler;
import com.networknt.http.HttpMethod;
import com.networknt.http.RequestEntity;
import com.networknt.http.ResponseEntity;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.Map;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class PetsGetHandler implements LightHttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(PetsGetHandler.class);
    PetsGetService service;

    public PetsGetHandler () {
        this.service = new PetsGetService ();
    }

    
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if(logger.isDebugEnabled()) logger.debug("handleRequest is called");
        HeaderMap requestHeaders = exchange.getRequestHeaders();
        Map<String, Deque<String>> queryParameters = exchange.getQueryParameters();
        Map<String, Deque<String>> pathParameters = exchange.getPathParameters();
        HttpMethod httpMethod = HttpMethod.resolve(exchange.getRequestMethod().toString());
        RequestEntity requestEntity = new RequestEntity<>(null, requestHeaders, httpMethod, null, null, queryParameters, pathParameters);
        ResponseEntity<String> responseEntity = service.invoke(requestEntity);
        responseEntity.getHeaders().forEach(values -> {
            exchange.getResponseHeaders().add(values.getHeaderName(), values.getFirst());
        });
        exchange.setStatusCode(responseEntity.getStatusCodeValue());
        exchange.getResponseSender().send(responseEntity.getBody());
    }
}
