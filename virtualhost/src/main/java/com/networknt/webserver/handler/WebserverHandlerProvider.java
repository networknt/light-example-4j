package com.networknt.webserver.handler;

import com.networknt.config.Config;
import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;


public class WebserverHandlerProvider implements HandlerProvider {

    public HttpHandler getHandler() {
        return Handlers.path().addPrefixPath("/json",
                new JsonHandler(Config.getInstance().getMapper()))
                .addPrefixPath("/text",
                        new TextHandler());
    }
}
