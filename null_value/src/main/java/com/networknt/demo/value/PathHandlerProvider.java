package com.networknt.demo.value;

import com.networknt.config.Config;
import com.networknt.handler.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import com.networknt.info.ServerInfoGetHandler;
import com.networknt.demo.value.handler.*;

public class PathHandlerProvider implements HandlerProvider {
    @Override
    public HttpHandler getHandler() {
        return Handlers.routing()
            .add(Methods.POST, "/v1/data", new DataPostHandler())
            .add(Methods.GET, "/v1/server/info", new ServerInfoGetHandler())
        ;
    }
}

