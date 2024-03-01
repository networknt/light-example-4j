package com.networknt.computerstore.handler;

import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.Map;

public class ComputersGetHandler implements LightHttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.setStatusCode(200);
        exchange.getResponseSender().send("[{\"id\":1,\"brand\":\"HP\",\"tag\":\"1112222-22\"},{\"id\":2,\"brand\":\"IBM\",\"tag\":\"111122255-55\"}]");
    }
}
