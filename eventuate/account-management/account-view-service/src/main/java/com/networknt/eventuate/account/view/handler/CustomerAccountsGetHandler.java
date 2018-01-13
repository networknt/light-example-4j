
package com.networknt.eventuate.account.view.handler;

import com.networknt.config.Config;
import com.networknt.eventuate.account.common.model.account.CreateAccountResponse;
import com.networknt.eventuate.account.common.model.account.GetAccountResponse;
import com.networknt.eventuate.queryservice.account.AccountInfo;
import com.networknt.eventuate.queryservice.account.AccountQueryService;
import com.networknt.service.SingletonServiceFactory;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


public class CustomerAccountsGetHandler implements HttpHandler {

    AccountQueryService service =
            (AccountQueryService) SingletonServiceFactory.getBean(AccountQueryService.class);
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        String customerId = exchange.getQueryParameters().get("customerId").getFirst();
        List<GetAccountResponse> result = service.findByCustomerId(customerId).stream().map(accountInfo-> new GetAccountResponse(
                                                           accountInfo.getId(),
                                                           new BigDecimal(accountInfo.getBalance()),
                                                           accountInfo.getTitle(),
                                                           accountInfo.getDescription())) .collect(Collectors.toList());

        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(result));
        
    }
}
