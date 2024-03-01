package com.networknt.bookstore.handler;

import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.Map;

public class BooksGetHandler implements LightHttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.setStatusCode(200);
        exchange.getResponseSender().send("[{\"id\":1,\"name\":\"Educated\",\"author\":\"James Bond\"},{\"id\":2,\"name\":\"The Amazon Job\",\"author\":\"Randy Grieser\"}]");
    }
}
