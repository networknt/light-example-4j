
package com.networknt.eventuate.customer.command;

import com.networknt.config.Config;
import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import com.networknt.info.ServerInfoGetHandler;
import com.networknt.health.HealthGetHandler;
import com.networknt.eventuate.customer.command.handler.*;

public class PathHandlerProvider implements HandlerProvider {
    @Override
    public HttpHandler getHandler() {
        return Handlers.routing()
        
            .add(Methods.DELETE, "/v1/toaccounts/{customerId}/{accountId}", new ToaccountsCustomerIdAccountIdDeleteHandler())
        
            .add(Methods.GET, "/v1/health", new HealthGetHandler())
        
            .add(Methods.POST, "/v1/createcustomer", new CreatecustomerPostHandler())
        
            .add(Methods.POST, "/v1/customers/toaccounts/{id}", new CustomersToaccountsIdPostHandler())
        
            .add(Methods.GET, "/v1/server/info", new ServerInfoGetHandler())
        
        ;
    }
}
