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
import java.util.Collections;
import java.util.List;

import com.networknt.genai.ollama.OllamaClient;
import com.networknt.genai.ChatMessage;
import com.networknt.genai.StreamCallback;
import com.networknt.genai.handler.ChatHistoryRepository;
import com.networknt.genai.handler.ChatSessionRepository;
import com.networknt.genai.handler.InMemoryChatHistoryRepository;
import com.networknt.genai.handler.InMemoryChatSessionRepository;
import com.networknt.genai.handler.ChatSession;

public class LlmClient {
    private static final Logger LOG = LoggerFactory.getLogger(LlmClient.class);
    private final OllamaClient ollamaClient = new OllamaClient();
    private final ChatSessionRepository sessionRepository = new InMemoryChatSessionRepository();
    private final ChatHistoryRepository historyRepository = new InMemoryChatHistoryRepository();

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

                                    // Serialize session management using channelId as effective session identifier
                                    // for now
                                    // in a real app, we might handshake userId.
                                    // For now, we assume a single user per channel connection or map channelId to
                                    // session.
                                    // Let's create a session.
                                    ChatSession session = sessionRepository.createSession("user-" + channelId,
                                            "llama3.2");
                                    String sessionId = session.getSessionId();

                                    channel.getReceiveSetter().set(new AbstractReceiveListener() {
                                        @Override
                                        protected void onFullTextMessage(WebSocketChannel channel,
                                                BufferedTextMessage message) {
                                            String data = message.getData();
                                            LOG.info("Received message: {}", data);

                                            // Add to history
                                            ChatMessage userMsg = new ChatMessage("user", data);
                                            historyRepository.addMessage(sessionId, userMsg);

                                            // Get full history
                                            List<ChatMessage> history = historyRepository.getHistory(sessionId);

                                            // Invoke Ollama with history
                                            ollamaClient.chatStream(history, new StreamCallback() {
                                                private final StringBuilder buffer = new StringBuilder();
                                                private final StringBuilder fullResponse = new StringBuilder();

                                                @Override
                                                public void onEvent(String content) {
                                                    try {
                                                        buffer.append(content);
                                                        fullResponse.append(content);
                                                        // Send only when newline is detected to simulate chunking
                                                        if (buffer.toString().contains("\n")) {
                                                            WebSockets.sendText(buffer.toString(), channel, null);
                                                            buffer.setLength(0);
                                                        }
                                                    } catch (Exception e) {
                                                        LOG.error("Error sending message chunk", e);
                                                    }
                                                }

                                                @Override
                                                public void onComplete() {
                                                    // Send remaining buffer
                                                    if (buffer.length() > 0) {
                                                        WebSockets.sendText(buffer.toString(), channel, null);
                                                    }
                                                    // Add assistant response to history
                                                    ChatMessage assistantMsg = new ChatMessage("assistant",
                                                            fullResponse.toString());
                                                    historyRepository.addMessage(sessionId, assistantMsg);
                                                }

                                                @Override
                                                public void onError(Throwable throwable) {
                                                    LOG.error("Ollama Error", throwable);
                                                    WebSockets.sendText("Error: " + throwable.getMessage(), channel,
                                                            null);
                                                }
                                            });
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
