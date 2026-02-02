package com.networknt.llmchat.handler;

import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import java.util.Deque;
import com.networknt.llmchat.client.LlmClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.XnioWorker;

public class ConnectHandler implements LightHttpHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectHandler.class);

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        LOG.info("ConnectHandler.handleRequest is called.");
        Deque<String> deque = exchange.getQueryParameters().get("channelId");
        String channelId = deque != null ? deque.getFirst() : null;
        if (channelId == null) {
            exchange.setStatusCode(400);
            exchange.getResponseSender().send("Missing channelId");
            return;
        }

        // Trigger Client Connection
        // Use the server's worker and buffer pool
        XnioWorker worker = exchange.getConnection().getWorker();
        io.undertow.connector.ByteBufferPool bufferPool = exchange.getConnection().getByteBufferPool();
        new LlmClient().connect(channelId, worker, bufferPool);

        exchange.getResponseSender().send("Connecting to Gateway for channel " + channelId);
    }
}
