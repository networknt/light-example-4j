package com.networknt.saga.example.order.service;



import com.networknt.example.sagas.ordersandcustomers.data.TramCommandsAndEventsIntegrationData;
import com.networknt.example.sagas.ordersandcustomers.order.domain.OrderRepository;
import com.networknt.example.sagas.ordersandcustomers.order.saga.createorder.CreateOrderSaga;
import com.networknt.example.sagas.ordersandcustomers.order.saga.createorder.CreateOrderSagaData;
import com.networknt.example.sagas.ordersandcustomers.order.service.OrderCommandHandler;
import com.networknt.example.sagas.ordersandcustomers.order.service.OrderService;
import com.networknt.saga.orchestration.Saga;
import com.networknt.saga.orchestration.SagaManager;
import com.networknt.saga.orchestration.SagaManagerImpl;
import com.networknt.service.SingletonServiceFactory;
import com.networknt.tram.command.common.ChannelMapping;
import com.networknt.tram.command.common.DefaultChannelMapping;

public class OrderServiceInitializer {

    public CreateOrderSaga createOrderSaga() {
        return new CreateOrderSaga();
    }


    public ChannelMapping channelMapping() {
        TramCommandsAndEventsIntegrationData data = SingletonServiceFactory.getBean(TramCommandsAndEventsIntegrationData.class);
        return DefaultChannelMapping.builder()
                .with("CustomerAggregate", data.getAggregateDestination())
                .with("customerService", data.getCommandChannel())
                .build();
    }

    public SagaManager<CreateOrderSagaData> createOrderSagaManager() {
        Saga<CreateOrderSagaData> saga = SingletonServiceFactory.getBean(CreateOrderSaga.class);
        return new SagaManagerImpl<>(saga);
    }


    public OrderService orderService() {
        OrderRepository orderRepository = SingletonServiceFactory.getBean(OrderRepository.class);
        SagaManager<CreateOrderSagaData> createOrderSagaDataSagaManager = SingletonServiceFactory.getBean(SagaManager.class);
        return new OrderService(orderRepository, createOrderSagaDataSagaManager);
    }

}
