package com.networknt.example.sagas.ordersandcustomers.customer.service;


import com.networknt.example.sagas.ordersandcustomers.customer.command.ReserveCreditCommand;
import com.networknt.example.sagas.ordersandcustomers.customer.domain.Customer;
import com.networknt.example.sagas.ordersandcustomers.customer.domain.CustomerCreditLimitExceededException;
import com.networknt.example.sagas.ordersandcustomers.customer.domain.CustomerRepository;
import com.networknt.example.sagas.ordersandcustomers.customer.replies.CustomerCreditReservationFailed;
import com.networknt.example.sagas.ordersandcustomers.customer.replies.CustomerCreditReserved;
import com.networknt.saga.participant.SagaCommandHandlersBuilder;
import com.networknt.tram.command.consumer.CommandHandlerReplyBuilder;
import com.networknt.tram.command.consumer.CommandHandlers;
import com.networknt.tram.command.consumer.CommandMessage;
import com.networknt.tram.message.common.Message;

public class CustomerCommandHandler {


  private CustomerRepository customerRepository;

  public CustomerCommandHandler(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public CommandHandlers commandHandlerDefinitions() {
    return SagaCommandHandlersBuilder
            .fromChannel("customerService")
            .onMessage(ReserveCreditCommand.class, this::reserveCredit)
            .build();
  }

  public Message reserveCredit(CommandMessage<ReserveCreditCommand> cm) {

    ReserveCreditCommand cmd = cm.getCommand();
    long customerId = cmd.getCustomerId();
    Customer customer = (Customer)customerRepository.findOne(customerId);
    // TODO null check
    try {
      customer.reserveCredit(cmd.getOrderId(), cmd.getOrderTotal());
      customerRepository.update(customer);
      return CommandHandlerReplyBuilder.withSuccess(new CustomerCreditReserved());

    } catch (CustomerCreditLimitExceededException e) {
      return CommandHandlerReplyBuilder.withFailure(new CustomerCreditReservationFailed());
    }
  }

  // withLock(Customer.class, customerId).
  // TODO @Validate to trigger validation and error reply


}
