package com.networknt.isoxml.handler;

import com.networknt.body.BodyHandler;
import com.networknt.config.Config;

import com.networknt.handler.LightHttpHandler;
import com.networknt.http.HttpMethod;
import com.networknt.http.HttpStatus;
import com.networknt.http.MediaType;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HeaderMap;

import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.Map;

/**
 * This is the handler that returns the pets in XML format with ISO-8859-1 encoding.
 *
 */
public class PetsGetHandler implements LightHttpHandler {


    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String responseBody = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<name>çáéíóú ÇÁÉÍÓÚ</name>";
        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
        exchange.setStatusCode(HttpStatus.OK.value());
        exchange.getResponseSender().send(responseBody, StandardCharsets.ISO_8859_1);
    }
}
