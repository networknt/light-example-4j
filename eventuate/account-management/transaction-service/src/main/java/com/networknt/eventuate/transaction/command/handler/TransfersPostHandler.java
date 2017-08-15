
package com.networknt.eventuate.transaction.command.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.body.BodyHandler;
import com.networknt.config.Config;
import com.networknt.eventuate.account.command.transaction.MoneyTransfer;
import com.networknt.eventuate.account.command.transaction.MoneyTransferService;
import com.networknt.eventuate.account.common.event.transaction.TransferDetails;
import com.networknt.eventuate.account.common.model.account.CreateAccountRequest;
import com.networknt.eventuate.account.common.model.account.CreateAccountResponse;
import com.networknt.eventuate.account.common.model.transaction.CreateMoneyTransferRequest;
import com.networknt.eventuate.account.common.model.transaction.CreateMoneyTransferResponse;
import com.networknt.eventuate.common.AggregateRepository;
import com.networknt.eventuate.common.EventuateAggregateStore;
import com.networknt.service.SingletonServiceFactory;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TransfersPostHandler implements HttpHandler {
    private EventuateAggregateStore eventStore  = (EventuateAggregateStore) SingletonServiceFactory.getBean(EventuateAggregateStore.class);
    private AggregateRepository transferRepository = new AggregateRepository(MoneyTransfer.class, eventStore);

    private MoneyTransferService service = new MoneyTransferService(transferRepository);

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        // add a new object
        Map s = (Map)exchange.getAttachment(BodyHandler.REQUEST_BODY);
        String json = mapper.writeValueAsString(s);
        CreateMoneyTransferRequest request = mapper.readValue(json, CreateMoneyTransferRequest.class);
        TransferDetails transferDetails = new TransferDetails(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount(),
                new Date(),
                request.getDescription());

        CompletableFuture<CreateMoneyTransferResponse> result = service.transferMoney(transferDetails).thenApply((e) -> {
            CreateMoneyTransferResponse m =  new CreateMoneyTransferResponse(e.getEntityId());
            return m;
        });

        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(result.get()));
        
    }
}
