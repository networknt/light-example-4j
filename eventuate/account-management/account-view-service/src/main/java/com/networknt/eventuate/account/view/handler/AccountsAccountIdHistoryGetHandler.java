
package com.networknt.eventuate.account.view.handler;

import com.networknt.config.Config;
import com.networknt.eventuate.account.common.model.account.AccountTransactionInfo;

import com.networknt.eventuate.queryservice.account.AccountQueryService;
import com.networknt.service.SingletonServiceFactory;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.List;


public class AccountsAccountIdHistoryGetHandler implements HttpHandler {
    AccountQueryService service =
            (AccountQueryService) SingletonServiceFactory.getBean(AccountQueryService.class);
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String accountId = exchange.getQueryParameters().get("accountId").getFirst();

        List<AccountTransactionInfo> result = service.getAccountTransactionHistory(accountId);

        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(result));
        
    }
}
