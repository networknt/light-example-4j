package com.networknt.eventuate.transaction.command;


import com.networknt.eventuate.account.command.transaction.MoneyTransfer;
import com.networknt.eventuate.account.command.transaction.RecordCreditCommand;
import com.networknt.eventuate.account.command.transaction.RecordDebitCommand;
import com.networknt.eventuate.account.command.transaction.RecordDebitFailedCommand;
import com.networknt.eventuate.account.common.event.account.AccountCreditedEvent;
import com.networknt.eventuate.account.common.event.account.AccountDebitFailedDueToInsufficientFundsEvent;
import com.networknt.eventuate.account.common.event.account.AccountDebitedEvent;
import com.networknt.eventuate.common.EntityWithIdAndVersion;
import com.networknt.eventuate.common.EventHandlerContext;
import com.networknt.eventuate.common.EventHandlerMethod;
import com.networknt.eventuate.common.EventSubscriber;

import java.util.concurrent.CompletableFuture;

@EventSubscriber(id="transferEventHandlers")
public class MoneyTransferWorkflow {

  @EventHandlerMethod
  public CompletableFuture<EntityWithIdAndVersion<MoneyTransfer>> recordDebit(EventHandlerContext<AccountDebitedEvent> ctx) {
    return ctx.update(MoneyTransfer.class, ctx.getEvent().getTransactionId(), new RecordDebitCommand());
  }

  @EventHandlerMethod
  public CompletableFuture<EntityWithIdAndVersion<MoneyTransfer>> recordDebitFailed(EventHandlerContext<AccountDebitFailedDueToInsufficientFundsEvent> ctx) {
    return ctx.update(MoneyTransfer.class, ctx.getEvent().getTransactionId(), new RecordDebitFailedCommand());
  }

  @EventHandlerMethod
  public CompletableFuture<EntityWithIdAndVersion<MoneyTransfer>> recordCredit(EventHandlerContext<AccountCreditedEvent> ctx) {
    return ctx.update(MoneyTransfer.class, ctx.getEvent().getTransactionId(), new RecordCreditCommand());
  }


}
