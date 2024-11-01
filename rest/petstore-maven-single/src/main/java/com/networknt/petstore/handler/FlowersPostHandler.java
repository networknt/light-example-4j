package com.networknt.petstore.handler;

import com.networknt.body.BodyHandler;
import com.networknt.config.Config;
import com.networknt.http.*;
import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.Map;
import java.util.stream.Collectors;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class FlowersPostHandler implements LightHttpHandler {
    public static final Logger logger = LoggerFactory.getLogger(FlowersPostHandler.class);
    public static final String CONTENT_TYPE_MISMATCH = "ERR10015";

    public FlowersPostHandler () {
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        HeaderMap requestHeaders = exchange.getRequestHeaders();
        if(logger.isInfoEnabled()) {
            // output all the headers.
            logger.info("requestHeaders = {}", requestHeaders.toString());
            // output the request body.
            InputStream bodyStream = (InputStream)exchange.getAttachment(BodyHandler.REQUEST_BODY);
            logger.info("body = {}", new BufferedReader(new InputStreamReader(bodyStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n")));
        }

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
