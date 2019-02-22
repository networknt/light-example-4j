package com.networknt.database;

import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.util.Methods;
import com.networknt.info.ServerInfoGetHandler;
import com.networknt.database.handler.*;

public class PathHandlerProvider implements HandlerProvider {
    @Override
    public HttpHandler getHandler() {
        return Handlers.routing()
            .add(Methods.GET, "/v1/queries", new QueriesGetHandler())
            .add(Methods.GET, "/v1/query", new QueryGetHandler())
            .add(Methods.GET, "/v1/server/info", new ServerInfoGetHandler())
            .add(Methods.GET, "/v1/updates", new UpdatesGetHandler())
        ;
    }
}

