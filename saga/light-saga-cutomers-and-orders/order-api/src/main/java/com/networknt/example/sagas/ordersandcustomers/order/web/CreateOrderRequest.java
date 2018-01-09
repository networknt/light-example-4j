package com.networknt.example.sagas.ordersandcustomers.order.web;


import com.networknt.example.sagas.ordersandcustomers.commondomain.Money;

public class CreateOrderRequest {
  private String orderTotal;
  private Long customerId;
  private Money orderTotalMoney;

  public CreateOrderRequest() {
  }

  public CreateOrderRequest(Long customerId, String orderTotal) {
    this.customerId = customerId;
    this.orderTotal = orderTotal;
  }

  public String getOrderTotal() {
    return orderTotal;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public Money getOrderTotalMoney() {
    return new Money(orderTotal);
  }
}
