package com.networknt.example.sagas.ordersandcustomers.order.service;


import com.networknt.example.sagas.ordersandcustomers.order.common.OrderDetails;
import com.networknt.example.sagas.ordersandcustomers.order.domain.Order;
import com.networknt.example.sagas.ordersandcustomers.order.domain.OrderRepository;
import com.networknt.example.sagas.ordersandcustomers.order.saga.createorder.CreateOrderSagaData;
import com.networknt.saga.orchestration.SagaManager;

import com.networknt.tram.event.ResultWithEvents;

public class OrderService {


  private SagaManager<CreateOrderSagaData> createOrderSagaManager;


  private OrderRepository orderRepository;

  public OrderService(OrderRepository orderRepository, SagaManager<CreateOrderSagaData> createOrderSagaManager) {
    this.orderRepository = orderRepository;
    this.createOrderSagaManager = createOrderSagaManager;
  }

  public Order createOrder(OrderDetails orderDetails) {
    ResultWithEvents<Order> oe = Order.createOrder(orderDetails);
    Order order = oe.result;
    orderRepository.save(order);
    CreateOrderSagaData data = new CreateOrderSagaData(order.getId(), orderDetails);
    createOrderSagaManager.create(data, Order.class, order.getId());

    return order;
  }

  public Order getOrderById(Long orderId) {
    return  orderRepository.findOne(orderId);
  }
}
