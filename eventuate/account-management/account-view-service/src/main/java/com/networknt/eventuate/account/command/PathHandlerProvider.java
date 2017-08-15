
package com.networknt.eventuate.account.command;

import com.networknt.config.Config;
import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import com.networknt.info.ServerInfoGetHandler;
import com.networknt.health.HealthGetHandler;
import com.networknt.eventuate.account.command.handler.*;

public class PathHandlerProvider implements HandlerProvider {
    @Override
    public HttpHandler getHandler() {
        return Handlers.routing()
        
            .add(Methods.GET, "/v1/accounts/{accountId}", new AccountsAccountIdGetHandler())

            .add(Methods.GET, "/v1/customer/accounts/{customerId}", new CustomerAccountsGetHandler())
        
            .add(Methods.GET, "/v1/accounts/{accountId}/history", new AccountsAccountIdHistoryGetHandler())
        
            .add(Methods.GET, "/v1/health", new HealthGetHandler())

            .add(Methods.GET, "/v1/server/info", new ServerInfoGetHandler())
        
        ;
    }
}
