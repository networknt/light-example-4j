package com.networknt.petstore.handler;

import com.networknt.body.BodyHandler;
import com.networknt.config.Config;
import com.networknt.http.*;
import com.networknt.handler.LightHttpHandler;
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

    public FlowersPostHandler () {
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        HeaderMap requestHeaders = exchange.getRequestHeaders();
        String contentType = null;
        HeaderValues contentTypeObject = requestHeaders.get(Headers.CONTENT_TYPE);
        if(contentTypeObject != null) contentType = contentTypeObject.getFirst();
        if(contentType == null || !contentType.startsWith("text/xml")) {
            setExchangeStatus(exchange, CONTENT_TYPE_MISMATCH, contentType);
            return;
        }
        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, MediaType.TEXT_XML_VALUE);
        String body = "<Flower><name>Poppy</name><color>RED</color><petals>9</petals></Flower>";
        exchange.setStatusCode(HttpStatus.OK.value());
        exchange.getResponseSender().send(body);
    }
}
