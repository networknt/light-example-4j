package com.networknt.market.handler;

import com.networknt.body.BodyHandler;
import com.networknt.config.Config;
import com.networknt.market.service.StoreProductsPostService;
import com.networknt.handler.LightHttpHandler;
import com.networknt.http.HttpMethod;
import com.networknt.http.RequestEntity;
import com.networknt.http.ResponseEntity;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import com.networknt.market.model.Product;
import java.util.Deque;
import java.util.Map;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class StoreProductsPostHandler implements LightHttpHandler {
    StoreProductsPostService service;

    public StoreProductsPostHandler () {
        this.service = new StoreProductsPostService ();
    }


    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        HeaderMap requestHeaders = exchange.getRequestHeaders();
        Map<String, Deque<String>> queryParameters = exchange.getQueryParameters();
        Map<String, Deque<String>> pathParameters = exchange.getPathParameters();
        HttpMethod httpMethod = HttpMethod.resolve(exchange.getRequestMethod().toString());
        Map<String, Object> bodyMap = (Map<String, Object>)exchange.getAttachment(BodyHandler.REQUEST_BODY);
        Product requestBody = Config.getInstance().getMapper().convertValue(bodyMap, Product.class);
        RequestEntity requestEntity = new RequestEntity<>(requestBody, requestHeaders, httpMethod, null, null, queryParameters, pathParameters);
        ResponseEntity<String> responseEntity = service.invoke(requestEntity);
        responseEntity.getHeaders().forEach(values -> {
            exchange.getResponseHeaders().add(values.getHeaderName(), values.getFirst());
        });
        exchange.setStatusCode(responseEntity.getStatusCodeValue());
        exchange.getResponseSender().send(responseEntity.getBody());
    }
}
