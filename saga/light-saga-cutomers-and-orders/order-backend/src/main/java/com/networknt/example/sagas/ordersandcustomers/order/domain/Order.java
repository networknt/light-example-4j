package com.networknt.example.sagas.ordersandcustomers.order.domain;


import com.networknt.example.sagas.ordersandcustomers.order.common.OrderDetails;
import com.networknt.tram.event.ResultWithEvents;

import java.util.Collections;


public class Order {


  private Long id;

  private OrderState state;


  private OrderDetails orderDetails;

  public Order() {
  }

  public Order(OrderDetails orderDetails) {
    //this.id = new IdGeneratorImpl().genId().getHi();
    this.orderDetails = orderDetails;
    this.state = OrderState.PENDING;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public static ResultWithEvents<Order> createOrder(OrderDetails orderDetails) {
    return new ResultWithEvents<Order>(new Order(orderDetails), Collections.emptyList());
  }

  public Long getId() {
    return id;
  }

  public void noteCreditReserved() {
    this.state = OrderState.APPROVED;
  }

  public void noteCreditReservationFailed() {
    this.state = OrderState.REJECTED;
  }

  public OrderState getState() {
    return state;
  }

  public void setState(OrderState state) {
    this.state = state;
  }

  public OrderDetails getOrderDetails() {
    return orderDetails;
  }
}
