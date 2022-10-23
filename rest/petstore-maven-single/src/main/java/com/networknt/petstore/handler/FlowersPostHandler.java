package com.networknt.petstore.handler;

import com.networknt.body.BodyHandler;
import com.networknt.config.Config;
import com.networknt.petstore.service.FlowersPostService;
import com.networknt.handler.LightHttpHandler;
import com.networknt.http.HttpMethod;
import com.networknt.http.RequestEntity;
import com.networknt.http.ResponseEntity;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;

import java.util.Deque;
import java.util.Map;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class FlowersPostHandler implements LightHttpHandler {
    public static final String CONTENT_TYPE_MISMATCH = "ERR10015";
    FlowersPostService service;

    public FlowersPostHandler () {
        this.service = new FlowersPostService ();
    }

    
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        HeaderMap requestHeaders = exchange.getRequestHeaders();
        // make sure that the content type header is text/xml
        String contentType = null;
        HeaderValues contentTypeObject = requestHeaders.get(Headers.CONTENT_TYPE);
        if(contentTypeObject != null) contentType = contentTypeObject.getFirst();
        if(contentType == null || !contentType.startsWith("text/xml")) {
            setExchangeStatus(exchange, CONTENT_TYPE_MISMATCH, contentType);
            return;
        }
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
