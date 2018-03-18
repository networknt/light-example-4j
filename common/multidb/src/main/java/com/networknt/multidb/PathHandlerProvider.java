
package com.networknt.multidb;

import com.networknt.config.Config;
import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import com.networknt.info.ServerInfoGetHandler;
import com.networknt.health.HealthGetHandler;

import com.networknt.multidb.handler.*;

public class PathHandlerProvider implements HandlerProvider {
    @Override
    public HttpHandler getHandler() {
        return Handlers.routing()
        
            .add(Methods.GET, "/health", new HealthGetHandler())
        
            .add(Methods.GET, "/server/info", new ServerInfoGetHandler())
        
            .add(Methods.GET, "/h2", new H2GetHandler())
        
            .add(Methods.GET, "/derby", new DerbyGetHandler())
        
        ;
    }
}
