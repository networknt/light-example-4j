
package com.networknt.eventuate.customer.view.handler;

import com.networknt.config.Config;

import com.networknt.eventuate.queryservice.customer.CustomerQueryService;
import com.networknt.eventuate.queryservice.customer.CustomersQueryResponse;
import com.networknt.eventuate.queryservice.customer.QuerySideCustomer;
import com.networknt.service.SingletonServiceFactory;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;


import java.util.concurrent.CompletableFuture;

public class CustomersCustomerIdGetHandler implements HttpHandler {

    CustomerQueryService service =
            (CustomerQueryService) SingletonServiceFactory.getBean(CustomerQueryService.class);
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        String id = exchange.getQueryParameters().get("customerId").getFirst();
        CompletableFuture<QuerySideCustomer> customerInfo = service.findByCustomerId(id);
        QuerySideCustomer response = null;
        if (!customerInfo.isCompletedExceptionally()) {
            response = customerInfo.get();
        }
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(response));
        
    }
}
