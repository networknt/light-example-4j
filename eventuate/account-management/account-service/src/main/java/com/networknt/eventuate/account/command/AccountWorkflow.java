package com.networknt.eventuate.account.command;


import com.networknt.eventuate.account.command.account.Account;
import com.networknt.eventuate.account.command.account.CreditAccountCommand;
import com.networknt.eventuate.account.command.account.DebitAccountCommand;
import com.networknt.eventuate.account.common.event.transaction.DebitRecordedEvent;
import com.networknt.eventuate.account.common.event.transaction.MoneyTransferCreatedEvent;
import com.networknt.eventuate.common.EntityWithIdAndVersion;
import com.networknt.eventuate.common.EventHandlerContext;
import com.networknt.eventuate.common.EventHandlerMethod;
import com.networknt.eventuate.common.EventSubscriber;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@EventSubscriber(id = "accountEventHandlers")
public class AccountWorkflow {

  public AccountWorkflow() {
  }

  @EventHandlerMethod
  public CompletableFuture<?> debitAccount(EventHandlerContext<MoneyTransferCreatedEvent> ctx) {
    MoneyTransferCreatedEvent event = ctx.getEvent();
    BigDecimal amount = event.getDetails().getAmount();
    String transactionId = ctx.getEntityId();

    String fromAccountId = event.getDetails().getFromAccountId();

    return ctx.update(Account.class, fromAccountId, new DebitAccountCommand(amount, transactionId));
  }

  @EventHandlerMethod
  public CompletableFuture<EntityWithIdAndVersion<Account>> creditAccount(EventHandlerContext<DebitRecordedEvent> ctx) {
    DebitRecordedEvent event = ctx.getEvent();
    BigDecimal amount = event.getDetails().getAmount();
    String fromAccountId = event.getDetails().getToAccountId();
    String transactionId = ctx.getEntityId();

    return ctx.update(Account.class, fromAccountId, new CreditAccountCommand(amount, transactionId));
  }
}
