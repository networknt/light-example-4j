package com.networknt.eventuate.account.common.event.customer;

public class CustomerToAccountDeleted implements CustomerEvent {

  private String accountId;

  public CustomerToAccountDeleted() {
  }

  public CustomerToAccountDeleted(String accountId) {
    this.accountId = accountId;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }
}
