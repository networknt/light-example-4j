package com.networknt.eventuate.account.common.event.account;

public class AccountDebitFailedDueToInsufficientFundsEvent implements AccountEvent {
  private String transactionId;

  private AccountDebitFailedDueToInsufficientFundsEvent() {
  }

  public AccountDebitFailedDueToInsufficientFundsEvent(String transactionId) {
    this.transactionId = transactionId;
  }

  public String getTransactionId() {
    return transactionId;
  }
}
