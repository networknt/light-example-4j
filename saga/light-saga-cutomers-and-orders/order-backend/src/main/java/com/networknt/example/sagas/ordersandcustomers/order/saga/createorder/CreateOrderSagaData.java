package com.networknt.example.sagas.ordersandcustomers.order.saga.createorder;


import com.networknt.example.sagas.ordersandcustomers.order.common.OrderDetails;

public class CreateOrderSagaData  {

  private Long orderId;

  private OrderDetails orderDetails;

  public CreateOrderSagaData() {
  }

  public CreateOrderSagaData(Long orderId, OrderDetails orderDetails) {
    this.orderId = orderId;
    this.orderDetails = orderDetails;
  }

  public CreateOrderSagaData(OrderDetails orderDetails) {
    this.orderDetails = orderDetails;
  }

  public Long getOrderId() {
    return orderId;
  }

  public OrderDetails getOrderDetails() {
    return orderDetails;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }
}
