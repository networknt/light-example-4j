package com.networknt.eventuate.account.command.transaction;

import com.networknt.eventuate.account.common.event.transaction.TransferDetails;
import com.networknt.eventuate.common.AggregateRepository;
import com.networknt.eventuate.common.EntityWithIdAndVersion;

import java.util.concurrent.CompletableFuture;

public class MoneyTransferService {
  private final AggregateRepository<MoneyTransfer, MoneyTransferCommand> aggregateRepository;

  public MoneyTransferService(AggregateRepository<MoneyTransfer, MoneyTransferCommand> aggregateRepository) {
    this.aggregateRepository = aggregateRepository;
  }

  public CompletableFuture<EntityWithIdAndVersion<MoneyTransfer>> transferMoney(TransferDetails transferDetails) {
    return aggregateRepository.save(new CreateMoneyTransferCommand(transferDetails));
  }

}
