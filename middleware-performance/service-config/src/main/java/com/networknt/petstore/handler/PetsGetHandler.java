
package com.networknt.petstore.handler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.Map;

public class PetsGetHandler implements HttpHandler {
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        
            exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
             exchange.getResponseSender().send("[{\"id\":1,\"name\":\"catten\",\"tag\":\"cat\"},{\"id\":2,\"name\":\"doggy\",\"tag\":\"dog\"}]");
        
    }
}
