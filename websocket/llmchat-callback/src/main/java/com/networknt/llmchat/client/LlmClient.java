package com.networknt.llmchat.client;

import io.undertow.connector.ByteBufferPool;
import io.undertow.websockets.client.WebSocketClient;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import org.xnio.IoFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.XnioWorker;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class LlmClient {
    private static final Logger LOG = LoggerFactory.getLogger(LlmClient.class);

    public void connect(String channelId, XnioWorker worker, ByteBufferPool bufferPool) {
        try {
            String url = "ws://localhost:9080/chat/connect?channelId=" + channelId;
            LOG.info("Connecting to Gateway via Undertow Client: {}", url);

            WebSocketClient.connectionBuilder(worker, bufferPool, new URI(url))
                    .connect()
                    .addNotifier(new IoFuture.Notifier<WebSocketChannel, Object>() {
                        @Override
                        public void notify(IoFuture<? extends WebSocketChannel> ioFuture, Object attachment) {
                            if (ioFuture.getStatus() == IoFuture.Status.DONE) {
                                try {
                                    WebSocketChannel channel = ioFuture.get();
                                    LOG.info("Connected to Gateway for channel: {}", channelId);

                                    channel.getReceiveSetter().set(new AbstractReceiveListener() {
                                        @Override
                                        protected void onFullTextMessage(WebSocketChannel channel,
                                                BufferedTextMessage message) {
                                            String data = message.getData();
                                            LOG.info("Received message: {}", data);
                                            // Mock LLM Response
                                            String response = "Response from Callback: " + data;
                                            WebSockets.sendText(response, channel, null);
                                        }

                                        @Override
                                        protected void onError(WebSocketChannel channel, Throwable error) {
                                            LOG.error("WebSocket Error", error);
                                        }

                                        @Override
                                        protected void onClose(WebSocketChannel webSocketChannel,
                                                io.undertow.websockets.core.StreamSourceFrameChannel channel)
                                                throws IOException {
                                            LOG.info("WebSocket Closed");
                                            super.onClose(webSocketChannel, channel);
                                        }
                                    });
                                    channel.resumeReceives();

                                } catch (IOException e) {
                                    LOG.error("Failed to connect", e);
                                }
                            } else {
                                LOG.error("Connection failed: {}", ioFuture.getStatus());
                                if (ioFuture.getException() != null) {
                                    LOG.error("Exception: ", ioFuture.getException());
                                }
                            }
                        }
                    }, null);

        } catch (URISyntaxException e) {
            LOG.error("Invalid URI", e);
        }
    }
}
