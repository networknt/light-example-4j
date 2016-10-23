package com.networknt.demo.handler;

import com.networknt.config.Config;
import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.PathTemplateHandler;


public class DemoHandlerProvider implements HandlerProvider {

    public HttpHandler getHandler() {
        return Handlers.path().addPrefixPath("/json",
                new JsonHandler(Config.getInstance().getMapper()))
                .addPrefixPath("/text",
                        new TextHandler());
    }
}
