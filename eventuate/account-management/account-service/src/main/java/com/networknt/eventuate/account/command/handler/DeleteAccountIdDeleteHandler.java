
package com.networknt.eventuate.account.command.handler;

import com.networknt.config.Config;
import com.networknt.eventuate.account.command.account.Account;
import com.networknt.eventuate.account.command.account.AccountService;
import com.networknt.eventuate.account.common.model.account.DeleteAccountResponse;
import com.networknt.eventuate.common.AggregateRepository;
import com.networknt.eventuate.common.EventuateAggregateStore;
import com.networknt.service.SingletonServiceFactory;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.util.concurrent.CompletableFuture;

public class DeleteAccountIdDeleteHandler implements HttpHandler {

    private EventuateAggregateStore eventStore  = (EventuateAggregateStore) SingletonServiceFactory.getBean(EventuateAggregateStore.class);
    private AggregateRepository accountRepository = new AggregateRepository(Account.class, eventStore);

    private AccountService service = new AccountService(accountRepository);
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String id = exchange.getQueryParameters().get("accountId").getFirst();

        CompletableFuture<DeleteAccountResponse> result = service.deleteAccount(id).thenApply((e) -> {
            DeleteAccountResponse m =  new DeleteAccountResponse(id);
            return m;
        });
        DeleteAccountResponse response = null;
        if (!result.isCompletedExceptionally()) {
            response = result.get();
        }

        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(response));

        
    }
}
