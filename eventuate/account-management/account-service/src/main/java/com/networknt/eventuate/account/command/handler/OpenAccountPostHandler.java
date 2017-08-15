
package com.networknt.eventuate.account.command.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.body.BodyHandler;
import com.networknt.config.Config;
import com.networknt.eventuate.account.command.account.Account;
import com.networknt.eventuate.account.command.account.AccountService;
import com.networknt.eventuate.account.common.model.account.CreateAccountRequest;
import com.networknt.eventuate.account.common.model.account.CreateAccountResponse;
import com.networknt.eventuate.common.AggregateRepository;
import com.networknt.eventuate.common.EventuateAggregateStore;
import com.networknt.service.SingletonServiceFactory;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class OpenAccountPostHandler implements HttpHandler {

    private EventuateAggregateStore eventStore  = (EventuateAggregateStore) SingletonServiceFactory.getBean(EventuateAggregateStore.class);
    private AggregateRepository accountRepository = new AggregateRepository(Account.class, eventStore);

    private AccountService service = new AccountService(accountRepository);

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        ObjectMapper mapper = new ObjectMapper();


        // add a new object
        Map s = (Map)exchange.getAttachment(BodyHandler.REQUEST_BODY);
        String json = mapper.writeValueAsString(s);
        CreateAccountRequest account = mapper.readValue(json, CreateAccountRequest.class);

        CompletableFuture<CreateAccountResponse>  result = service.openAccount(account.getCustomerId(), account.getTitle(), account.getInitialBalance(), account.getDescription()).thenApply((e) -> {
            CreateAccountResponse m =  new CreateAccountResponse(e.getEntityId(), e.getAggregate().getBalance());
            return m;
        });

        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(result.get()));

    }
}
