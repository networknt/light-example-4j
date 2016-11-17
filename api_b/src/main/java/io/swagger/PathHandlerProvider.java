package io.swagger;

import com.networknt.config.Config;
import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import io.swagger.handler.*;

public class PathHandlerProvider implements HandlerProvider {

    public HttpHandler getHandler() {
        HttpHandler handler = Handlers.routing()
            .add(Methods.GET, "/v1/data", new DataGetHandler())
        ;
        return handler;
    }
}

