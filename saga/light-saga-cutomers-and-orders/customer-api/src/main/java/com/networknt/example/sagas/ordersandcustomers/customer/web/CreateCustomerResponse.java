package com.networknt.example.sagas.ordersandcustomers.customer.web;


public class CreateCustomerResponse {
  private Long customerId;

  public CreateCustomerResponse() {
  }

  public CreateCustomerResponse(Long customerId) {
    this.customerId = customerId;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }
}
