package com.networknt.eventuate.queryservice.account;

public class AccountNotFoundException extends RuntimeException {

  public AccountNotFoundException(String accountId) {
    super("Account not found " + accountId);
  }
}
