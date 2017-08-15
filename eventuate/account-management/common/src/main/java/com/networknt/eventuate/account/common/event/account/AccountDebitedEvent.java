package com.networknt.eventuate.account.common.event.account;

import java.math.BigDecimal;

public class AccountDebitedEvent extends AccountChangedEvent {

  private AccountDebitedEvent() {
  }

  public AccountDebitedEvent(BigDecimal amount, String transactionId) {
    super(amount, transactionId);
  }

}
