package com.networknt.example.handler;

import com.networknt.health.HealthHandler;
import com.networknt.info.ServerInfoHandler;
import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.util.Methods;


public class ExampleHandlerProvider implements HandlerProvider {
    public HttpHandler getHandler() {
        HttpHandler handler = Handlers.routing()
                .add(Methods.GET, "/server/info", new ServerInfoHandler())
                .add(Methods.GET, "/server/health", new HealthHandler());
        return handler;
    }
}
