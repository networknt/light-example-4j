
package com.networknt.eventuate.customer.view;

import com.networknt.config.Config;
import com.networknt.handler.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import com.networknt.info.ServerInfoGetHandler;
import com.networknt.health.HealthGetHandler;
import com.networknt.eventuate.customer.view.handler.*;

public class PathHandlerProvider implements HandlerProvider {
    @Override
    public HttpHandler getHandler() {
        return Handlers.routing()
        
            .add(Methods.GET, "/v1/customer/{customerId}", new CustomersCustomerIdGetHandler())
        
            .add(Methods.GET, "/v1/health", new HealthGetHandler())
        
            .add(Methods.GET, "/v1/customers/{email}", new CustomersEmailGetHandler())
        
            .add(Methods.GET, "/v1/server/info", new ServerInfoGetHandler())
        
        ;
    }
}
