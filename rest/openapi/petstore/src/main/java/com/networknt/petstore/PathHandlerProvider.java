
package com.networknt.petstore;

import com.networknt.config.Config;
import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import com.networknt.info.ServerInfoGetHandler;
import com.networknt.health.HealthGetHandler;
import com.networknt.petstore.handler.*;

public class PathHandlerProvider implements HandlerProvider {
    @Override
    public HttpHandler getHandler() {
        return Handlers.routing()
        
            .add(Methods.GET, "/v1/health", new HealthGetHandler())
        
            .add(Methods.GET, "/v1/server/info", new ServerInfoGetHandler())
        
            .add(Methods.POST, "/v1/pets", new PetsPostHandler())
        
            .add(Methods.GET, "/v1/pets", new PetsGetHandler())
        
            .add(Methods.GET, "/v1/pets/{petId}", new PetsPetIdGetHandler())
        
            .add(Methods.DELETE, "/v1/pets/{petId}", new PetsPetIdDeleteHandler())
        
        ;
    }
}
