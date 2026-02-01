package com.networknt.llmchat.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LlmClient {
    private static final Logger LOG = LoggerFactory.getLogger(LlmClient.class);

    public void connect(String channelId) {
        HttpClient client = HttpClient.newHttpClient();
        // Gateway URL: ws://localhost:9080/connect?channelId=...
        // Assuming Gateway is running on port 9080 (HTTP) based on recent changes
        String url = "ws://localhost:9080/connect?channelId=" + channelId;
        LOG.info("Connecting to Gateway: {}", url);

        client.newWebSocketBuilder()
                .buildAsync(URI.create(url), new WebSocket.Listener() {
                    @Override
                    public void onOpen(WebSocket webSocket) {
                        LOG.info("Connected to Gateway for channel: {}", channelId);
                        WebSocket.Listener.super.onOpen(webSocket);
                    }

                    @Override
                    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                        LOG.info("Received message: {}", data);
                        // Simple Echo/Mock Response to demonstrate the callback pattern
                        // In a real scenario, this would invoke the LLM
                        String response = "Response from Callback: " + data;
                        return webSocket.sendText(response, true);
                    }

                    @Override
                    public void onError(WebSocket webSocket, Throwable error) {
                        LOG.error("WebSocket Error", error);
                    }

                    @Override
                    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
                        LOG.info("WebSocket Closed: {} {}", statusCode, reason);
                        return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
                    }
                });
    }
}
