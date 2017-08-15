package com.networknt.eventuate.account.common.model.account;


import java.math.BigDecimal;

public class CreateAccountResponse {
  
  private String accountId;
  private BigDecimal balance;

  public CreateAccountResponse() {
  }

  public CreateAccountResponse(String accountId, BigDecimal balance) {
    this.accountId = accountId;
    this.balance = balance;
  }

  public CreateAccountResponse(String accountId) {
    this.accountId = accountId;
  }


  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }
}
