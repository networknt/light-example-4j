package com.networknt.saga.integration.test;


import com.networknt.example.sagas.ordersandcustomers.customer.domain.CustomerRepository;
import com.networknt.example.sagas.ordersandcustomers.customer.service.CustomerCommandHandler;
import com.networknt.example.sagas.ordersandcustomers.customer.service.CustomerService;
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

public class OrderCustomerServiceInitializer {

    public CreateOrderSaga createOrderSaga() {
        return new CreateOrderSaga();
    }

    public OrderCommandHandler orderCommandHandler() {
        OrderRepository orderRepository = SingletonServiceFactory.getBean(OrderRepository.class);
        return new OrderCommandHandler(orderRepository);
    }

    public TramCommandsAndEventsIntegrationData tramCommandsAndEventsIntegrationData() {
        return new TramCommandsAndEventsIntegrationData();
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

    public CustomerService customerService() {
        CustomerRepository customerRepository = SingletonServiceFactory.getBean(CustomerRepository.class);
        return new CustomerService(customerRepository);
    }

    public CustomerCommandHandler customerCommandHandler() {
        CustomerRepository customerRepository = SingletonServiceFactory.getBean(CustomerRepository.class);
        return new CustomerCommandHandler(customerRepository);
    }
}
