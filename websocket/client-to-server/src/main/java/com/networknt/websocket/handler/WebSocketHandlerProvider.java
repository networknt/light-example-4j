package com.networknt.websocket.handler;

import com.networknt.server.HandlerProvider;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;
import static io.undertow.Handlers.path;
import static io.undertow.Handlers.resource;
import static io.undertow.Handlers.websocket;

public class WebSocketHandlerProvider implements HandlerProvider {
    @Override
    public HttpHandler getHandler() {
        return path()
                .addPrefixPath("/myapp", websocket(new WebSocketConnectionCallback() {

                    @Override
                    public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
                        channel.getReceiveSetter().set(new AbstractReceiveListener() {

                            @Override
                            protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {
                                WebSockets.sendText(message.getData(), channel, null);
                            }
                        });
                        channel.resumeReceives();
                    }
                }))
                .addPrefixPath("/", resource(new ClassPathResourceManager(WebSocketHandlerProvider.class.getClassLoader(), "public")).addWelcomeFiles("index.html"));
    }
}
