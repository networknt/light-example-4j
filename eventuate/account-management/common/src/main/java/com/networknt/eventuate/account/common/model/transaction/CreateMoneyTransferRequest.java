package com.networknt.eventuate.account.common.model.transaction;

import java.math.BigDecimal;

public class CreateMoneyTransferRequest {


  private String fromAccountId;

  private String toAccountId;

  private BigDecimal amount;

  private String description;

  public CreateMoneyTransferRequest() {
  }

  public CreateMoneyTransferRequest(String fromAccountId, String toAccountId, BigDecimal amount, String description) {
    this.fromAccountId = fromAccountId;
    this.toAccountId = toAccountId;
    this.amount = amount;
    this.description = description;
  }

  public void setFromAccountId(String fromAccountId) {
    this.fromAccountId = fromAccountId;
  }

  public void setToAccountId(String toAccountId) {
    this.toAccountId = toAccountId;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getFromAccountId() {
    return fromAccountId;
  }

  public String getToAccountId() {
    return toAccountId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public String getDescription() {
    return description;
  }
}
