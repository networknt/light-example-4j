package com.networknt.eventuate.account.common.model.account;


import java.math.BigDecimal;

public class CreateAccountRequest {


  private String customerId;

  private String title;

  private String description;


  private BigDecimal initialBalance;

  public CreateAccountRequest() {
  }

  public CreateAccountRequest(String customerId, String title, String description, BigDecimal initialBalance) {
    this.customerId = customerId;
    this.title = title;
    this.description = description;
    this.initialBalance = initialBalance;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public BigDecimal getInitialBalance() {
    return initialBalance;
  }

  public void setInitialBalance(BigDecimal initialBalance) {
    this.initialBalance = initialBalance;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
