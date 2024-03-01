
package com.networknt.ab.handler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.Map;

public class DataGetHandler implements HttpHandler {
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

            exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
             exchange.getResponseSender().send("[\"Message 1\",\"Message 2\"]");

    }
}
