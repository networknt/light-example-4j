package com.networknt.eventuate.account.common.event.transaction;


public class CreditRecordedEvent implements MoneyTransferEvent {
  private TransferDetails details;

  private CreditRecordedEvent() {
  }

  public CreditRecordedEvent(TransferDetails details) {
    this.details = details;
  }

  public TransferDetails getDetails() {
    return details;
  }
}
