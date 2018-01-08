package com.networknt.example.sagas.ordersandcustomers.customer.web;


import com.networknt.example.sagas.ordersandcustomers.commondomain.Money;

public class CreateCustomerRequest {
  private String name;
  private Money creditLimit;

  public CreateCustomerRequest() {
  }

  public CreateCustomerRequest(String name, Money creditLimit) {

    this.name = name;
    this.creditLimit = creditLimit;
  }


  public String getName() {
    return name;
  }

  public Money getCreditLimit() {
    return creditLimit;
  }
}
