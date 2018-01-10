package com.networknt.apic.handler;

import com.networknt.config.Config;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.util.ArrayList;
import java.util.List;

public class DataGetHandler implements HttpHandler {
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        List<String> messages = new ArrayList<>();
        messages.add("API C: Message 1");
        messages.add("API C: Message 2");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(messages));
    }
}
