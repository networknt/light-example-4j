package com.networknt.eventuate.account.common.event.customer;

import com.networknt.eventuate.account.common.model.customer.ToAccountInfo;

public class CustomerAddedToAccount implements CustomerEvent {

  private ToAccountInfo toAccountInfo;

  public CustomerAddedToAccount() {
  }

  public CustomerAddedToAccount(ToAccountInfo toAccountInfo) {
    this.toAccountInfo = toAccountInfo;
  }

  public ToAccountInfo getToAccountInfo() {
    return toAccountInfo;
  }

  public void setToAccountInfo(ToAccountInfo toAccountInfo) {
    this.toAccountInfo = toAccountInfo;
  }
}
