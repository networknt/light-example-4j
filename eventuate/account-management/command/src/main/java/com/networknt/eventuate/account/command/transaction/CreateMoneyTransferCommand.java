package com.networknt.eventuate.account.command.transaction;


import com.networknt.eventuate.account.common.event.transaction.TransferDetails;

public class CreateMoneyTransferCommand implements MoneyTransferCommand {
  private TransferDetails details;

  public TransferDetails getDetails() {
    return details;
  }

  public CreateMoneyTransferCommand(TransferDetails details) {

    this.details = details;
  }
}
