
package com.networknt.eventuate.customer.command.handler;

import com.networknt.config.Config;
import com.networknt.eventuate.account.command.customer.Customer;
import com.networknt.eventuate.account.command.customer.CustomerService;
import com.networknt.eventuate.common.AggregateRepository;
import com.networknt.eventuate.common.EventuateAggregateStore;
import com.networknt.service.SingletonServiceFactory;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ToaccountsCustomerIdAccountIdDeleteHandler implements HttpHandler {
    private EventuateAggregateStore eventStore  = (EventuateAggregateStore) SingletonServiceFactory.getBean(EventuateAggregateStore.class);
    private AggregateRepository customertRepository = new AggregateRepository(Customer.class, eventStore);

    private CustomerService service = new CustomerService(customertRepository);
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String customerId = exchange.getQueryParameters().get("customerId").getFirst();
        String accountId = exchange.getQueryParameters().get("accountId").getFirst();

        System.out.println("customerId:" + customerId);
        System.out.println("accountId:" + accountId);

        CompletableFuture<String> result = service.deleteToAccount(customerId, accountId).thenApply((e) -> {
            String m =  e.getEntityId();
            return m;
        });
        String response = null;
        if (!result.isCompletedExceptionally()) {
            response = result.get();
        }
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(response));

    }
}
