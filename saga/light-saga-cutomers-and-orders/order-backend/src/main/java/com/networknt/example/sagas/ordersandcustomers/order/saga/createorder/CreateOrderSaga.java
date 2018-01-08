package com.networknt.example.sagas.ordersandcustomers.order.saga.createorder;


import com.networknt.example.sagas.ordersandcustomers.commondomain.Money;
import com.networknt.example.sagas.ordersandcustomers.customer.command.ReserveCreditCommand;
import com.networknt.example.sagas.ordersandcustomers.order.command.ApproveOrderCommand;
import com.networknt.example.sagas.ordersandcustomers.order.command.RejectOrderCommand;
import com.networknt.saga.dsl.SimpleSaga;
import com.networknt.saga.orchestration.SagaDefinition;
import com.networknt.tram.command.consumer.CommandWithDestination;

import static com.networknt.tram.command.consumer.CommandWithDestinationBuilder.send;

public class CreateOrderSaga implements SimpleSaga<CreateOrderSagaData> {

  private SagaDefinition<CreateOrderSagaData> sagaDefinition =
          step()
            .withCompensation(this::reject)
          .step()
            .invokeParticipant(this::reserveCredit)
          .step()
            .invokeParticipant(this::approve)
          .build();


  @Override
  public SagaDefinition<CreateOrderSagaData> getSagaDefinition() {
    return this.sagaDefinition;
  }


  private CommandWithDestination reserveCredit(CreateOrderSagaData data) {
    long orderId = data.getOrderId();
    Long customerId = data.getOrderDetails().getCustomerId();
    Money orderTotal = data.getOrderDetails().getOrderTotal();
    return send(new ReserveCreditCommand(customerId, orderId, orderTotal))
            .to("customerService")
            .build();
  }

  public CommandWithDestination reject(CreateOrderSagaData data) {
    return send(new RejectOrderCommand(data.getOrderId()))
            .to("orderService")
            .build();
  }

  private CommandWithDestination approve(CreateOrderSagaData data) {
    return send(new ApproveOrderCommand(data.getOrderId()))
            .to("orderService")
            .build();
  }


}
