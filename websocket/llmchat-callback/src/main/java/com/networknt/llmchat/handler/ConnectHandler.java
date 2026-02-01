package com.networknt.llmchat.handler;

import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import java.util.Deque;
import com.networknt.llmchat.client.LlmClient;

public class ConnectHandler implements LightHttpHandler {
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Deque<String> deque = exchange.getQueryParameters().get("channelId");
        String channelId = deque != null ? deque.getFirst() : null;
        if (channelId == null) {
            exchange.setStatusCode(400);
            exchange.getResponseSender().send("Missing channelId");
            return;
        }

        // Trigger Client Connection
        // Run asynchronously to not block HTTP response
        new Thread(() -> {
            new LlmClient().connect(channelId);
        }).start();

        exchange.getResponseSender().send("Connecting to Gateway for channel " + channelId);
    }
}
