package com.networknt.eventuate.account.common.event.transaction;


public class MoneyTransferCreatedEvent implements MoneyTransferEvent {
  private TransferDetails details;

  private MoneyTransferCreatedEvent() {
  }

  public MoneyTransferCreatedEvent(TransferDetails details) {
    this.details = details;
  }

  public TransferDetails getDetails() {
    return details;
  }
}

