package com.networknt.ac.handler;

import com.networknt.config.Config;
import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataGetHandler implements LightHttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        List<String> messages = new ArrayList<>();
        messages.add("API C: Message 1");
        messages.add("API C: Message 2");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(messages));
    }
}
