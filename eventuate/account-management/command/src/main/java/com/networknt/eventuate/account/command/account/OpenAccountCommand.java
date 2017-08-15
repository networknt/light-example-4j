package com.networknt.eventuate.account.command.account;


import java.math.BigDecimal;

public class OpenAccountCommand implements AccountCommand {

  private String customerId;
  private String title;
  private BigDecimal initialBalance;
  private String description;

  public OpenAccountCommand(String customerId, String title, BigDecimal initialBalance, String description) {
    this.customerId = customerId;
    this.title = title;
    this.initialBalance = initialBalance;
    this.description = description;
  }

  public BigDecimal getInitialBalance() {
    return initialBalance;
  }

  public String getCustomerId() {
    return customerId;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }
}
