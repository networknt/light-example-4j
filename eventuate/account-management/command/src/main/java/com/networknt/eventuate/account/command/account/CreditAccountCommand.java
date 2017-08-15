package com.networknt.eventuate.account.command.account;

import java.math.BigDecimal;

public class CreditAccountCommand implements AccountCommand {
  private final BigDecimal amount;
  private final String transactionId;

  public CreditAccountCommand(BigDecimal amount, String transactionId) {

    this.amount = amount;
    this.transactionId = transactionId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public String getTransactionId() {
    return transactionId;
  }
}
