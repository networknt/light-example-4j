package com.networknt.eventuate.account.common.event.account;

import java.math.BigDecimal;

public class AccountChangedEvent implements AccountEvent {
  protected BigDecimal amount;
  protected String transactionId;

  public AccountChangedEvent(BigDecimal amount, String transactionId) {
    this.amount = amount;
    this.transactionId = transactionId;
  }

  public AccountChangedEvent() {
  }

  public String getTransactionId() {
    return transactionId;
  }

  public BigDecimal getAmount() {
    return amount;
  }
}
