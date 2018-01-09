package com.networknt.example.sagas.ordersandcustomers.customer.web;


import com.networknt.example.sagas.ordersandcustomers.commondomain.Money;

public class CreateCustomerRequest {
  private String name;
  private Money creditLimitMoney;
  private String creditLimit;
  public CreateCustomerRequest() {
  }

  public CreateCustomerRequest(String name, String creditLimit) {

    this.name = name;
    this.creditLimit = creditLimit;
  }


  public String getName() {
    return name;
  }

  public String getCreditLimit() {
    return creditLimit;
  }

  public Money getCreditLimitMoney() {
    return new Money(creditLimit);
  }
}
