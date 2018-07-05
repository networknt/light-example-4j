
package com.networknt.eventuate.account.view;

import com.networknt.eventuate.account.view.handler.AccountsAccountIdGetHandler;
import com.networknt.eventuate.account.view.handler.AccountsAccountIdHistoryGetHandler;
import com.networknt.eventuate.account.view.handler.CustomerAccountsGetHandler;
import com.networknt.handler.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.util.Methods;
import com.networknt.info.ServerInfoGetHandler;
import com.networknt.health.HealthGetHandler;
import com.networknt.eventuate.account.view.handler.*;

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
