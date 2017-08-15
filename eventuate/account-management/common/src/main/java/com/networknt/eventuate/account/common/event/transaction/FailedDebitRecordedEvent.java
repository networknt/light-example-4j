package com.networknt.eventuate.account.common.event.transaction;


public class FailedDebitRecordedEvent implements MoneyTransferEvent {
  private TransferDetails details;

  private FailedDebitRecordedEvent() {
  }

  public FailedDebitRecordedEvent(TransferDetails details) {
    this.details = details;
  }

  public TransferDetails getDetails() {
    return details;
  }
}
