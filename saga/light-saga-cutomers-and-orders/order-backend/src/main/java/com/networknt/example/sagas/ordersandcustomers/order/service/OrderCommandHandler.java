package com.networknt.example.sagas.ordersandcustomers.order.service;


;
import com.networknt.example.sagas.ordersandcustomers.order.command.ApproveOrderCommand;
import com.networknt.example.sagas.ordersandcustomers.order.command.RejectOrderCommand;
import com.networknt.example.sagas.ordersandcustomers.order.domain.Order;
import com.networknt.example.sagas.ordersandcustomers.order.domain.OrderRepository;
import com.networknt.saga.participant.SagaCommandHandlersBuilder;
import com.networknt.tram.command.consumer.CommandHandlers;
import com.networknt.tram.command.consumer.CommandMessage;
import com.networknt.tram.message.common.Message;

import static com.networknt.tram.command.consumer.CommandHandlerReplyBuilder.withSuccess;


public class OrderCommandHandler {

  private OrderRepository orderRepository;

  public OrderCommandHandler(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  public CommandHandlers commandHandlerDefinitions() {
    return SagaCommandHandlersBuilder
            .fromChannel("orderService")
            .onMessage(ApproveOrderCommand.class, this::approve)
            .onMessage(RejectOrderCommand.class, this::reject)
            .build();
  }

  public Message approve(CommandMessage<ApproveOrderCommand> cm) {
    long orderId = cm.getCommand().getOrderId();
    Order order = orderRepository.findOne(orderId);
    order.noteCreditReserved();
    System.out.println("order with new status-->" + order.getState().name());
    orderRepository.update(order);
    return withSuccess();
  }

  public Message reject(CommandMessage<RejectOrderCommand> cm) {
    long orderId = cm.getCommand().getOrderId();
    Order order = orderRepository.findOne(orderId);
    order.noteCreditReservationFailed();
    System.out.println("order with new status-->" + order.getState().name());
    orderRepository.update(order);
    return withSuccess();
  }

}
