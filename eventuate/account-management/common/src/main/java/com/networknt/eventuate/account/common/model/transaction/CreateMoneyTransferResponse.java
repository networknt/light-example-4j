package com.networknt.eventuate.account.common.model.transaction;


public class CreateMoneyTransferResponse {
  private String moneyTransferId;

  public CreateMoneyTransferResponse() {
  }

  public CreateMoneyTransferResponse(String moneyTransferId) {

    this.moneyTransferId = moneyTransferId;
  }

  public String getMoneyTransferId() {
    return moneyTransferId;
  }
}
