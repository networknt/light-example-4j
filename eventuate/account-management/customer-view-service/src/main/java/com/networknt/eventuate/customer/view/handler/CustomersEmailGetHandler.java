
package com.networknt.eventuate.customer.view.handler;

import com.networknt.config.Config;
import com.networknt.eventuate.queryservice.customer.CustomerQueryService;
import com.networknt.eventuate.queryservice.customer.CustomersQueryResponse;
import com.networknt.eventuate.queryservice.customer.QuerySideCustomer;
import com.networknt.service.SingletonServiceFactory;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.List;

import java.util.concurrent.CompletableFuture;

public class CustomersEmailGetHandler implements HttpHandler {
    CustomerQueryService service =
            (CustomerQueryService) SingletonServiceFactory.getBean(CustomerQueryService.class);
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        String email = exchange.getQueryParameters().get("email").getFirst();
        CompletableFuture<List<QuerySideCustomer>> customerInfos = service.findByEmail(email);

        CustomersQueryResponse  response =null;
        if (!customerInfos.isCompletedExceptionally()) {
            response = new CustomersQueryResponse (customerInfos.get());
        }

        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(response));

        exchange.endExchange();
    }
}
