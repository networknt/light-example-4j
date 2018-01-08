package com.networknt.example.sagas.ordersandcustomers.customer.domain;



import com.networknt.example.sagas.ordersandcustomers.commondomain.Money;

import java.util.HashMap;
import java.util.Map;


public class Customer {


  private Long id;
  private String name;


  private Money creditLimit;


  private Map<Long, Money> creditReservations;

  Money availableCredit() {
    return creditLimit.subtract(creditReservations.values().stream().reduce(Money.ZERO, Money::add));
  }

  public Customer() {
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Customer(String name, Money creditLimit) {
  //  this.id = new IdGeneratorImpl().genId().getHi();
    this.name = name;
    this.creditLimit = creditLimit;
  //  this.creditReservations = Collections.emptyMap();
    this.creditReservations = new HashMap<>();
  }

  public Long getId() {
    return id;
  }

  public void reserveCredit(Long orderId, Money orderTotal) {
    if (availableCredit().isGreaterThanOrEqual(orderTotal)) {
      creditReservations.put(orderId, orderTotal);
    } else
      throw new CustomerCreditLimitExceededException();
  }
}
