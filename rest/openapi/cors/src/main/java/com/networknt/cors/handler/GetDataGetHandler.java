package com.networknt.cors.handler;

import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.Map;

public class GetDataGetHandler implements LightHttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

            exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
             exchange.getResponseSender().send("[{\"key\":\"key1\",\"value\":\"value1\"},{\"key\":\"key2\",\"value\":\"value2\"}]");

    }
}
