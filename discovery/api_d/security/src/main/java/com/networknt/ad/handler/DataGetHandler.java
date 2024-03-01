package com.networknt.ad.handler;

import com.networknt.config.Config;
import com.networknt.handler.LightHttpHandler;
import com.networknt.server.Server;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataGetHandler implements LightHttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        int port = Server.getServerConfig().getHttpsPort();
        List<String> messages = new ArrayList<>();
        messages.add("API D: Message 1 from port " + port);
        messages.add("API D: Message 2 from port " + port);
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(messages));
    }
}
