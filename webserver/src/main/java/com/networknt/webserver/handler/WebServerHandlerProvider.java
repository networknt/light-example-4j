package com.networknt.webserver.handler;

import com.networknt.config.Config;
import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;


public class WebServerHandlerProvider implements HandlerProvider {

    public HttpHandler getHandler() {
        return Handlers.path().addPrefixPath("/api/json",
                new JsonHandler(Config.getInstance().getMapper()))
                .addPrefixPath("/api/text",
                        new TextHandler());
    }
}
