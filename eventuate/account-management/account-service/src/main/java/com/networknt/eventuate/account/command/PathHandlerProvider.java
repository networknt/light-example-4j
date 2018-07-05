
package com.networknt.eventuate.account.command;

import com.networknt.handler.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;

import io.undertow.util.Methods;
import com.networknt.info.ServerInfoGetHandler;
import com.networknt.health.HealthGetHandler;
import com.networknt.eventuate.account.command.handler.*;

public class PathHandlerProvider implements HandlerProvider {
    @Override
    public HttpHandler getHandler() {
        return Handlers.routing()
        
            .add(Methods.POST, "/v1/openaccount", new OpenAccountPostHandler())
        
            .add(Methods.DELETE, "/v1/delete/{accountId}", new DeleteAccountIdDeleteHandler())
        
            .add(Methods.GET, "/v1/health", new HealthGetHandler())
        
            .add(Methods.GET, "/v1/server/info", new ServerInfoGetHandler())
        
        ;
    }
}
