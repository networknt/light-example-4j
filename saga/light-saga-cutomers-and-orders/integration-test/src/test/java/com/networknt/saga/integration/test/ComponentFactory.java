package com.networknt.saga.integration.test;

import com.networknt.example.sagas.ordersandcustomers.customer.service.CustomerCommandHandler;

import com.networknt.example.sagas.ordersandcustomers.order.service.OrderCommandHandler;
import com.networknt.saga.participant.SagaCommandDispatcher;
import com.networknt.saga.participant.SagaLockManager;
import com.networknt.service.SingletonServiceFactory;
import com.networknt.tram.command.common.ChannelMapping;
import com.networknt.tram.command.consumer.CommandDispatcher;
import com.networknt.tram.message.consumer.MessageConsumer;
import com.networknt.tram.message.producer.MessageProducer;


/**
 * Created by chenga on 2017-11-09.
 */
public class ComponentFactory {

    public static CommandDispatcher getCustomerCommandDispatcher() {
        CustomerCommandHandler customerCommandHandler = SingletonServiceFactory.getBean(CustomerCommandHandler.class);
        ChannelMapping channelMapping = SingletonServiceFactory.getBean(ChannelMapping.class);
        MessageProducer messageProducer =  SingletonServiceFactory.getBean(MessageProducer.class);
        MessageConsumer messageConsumer =  SingletonServiceFactory.getBean(MessageConsumer.class);
        SagaLockManager sagaLockManager = SingletonServiceFactory.getBean(SagaLockManager.class);
        return new SagaCommandDispatcher("customerCommandDispatcher",
                customerCommandHandler.commandHandlerDefinitions(),
                channelMapping,
                messageConsumer,
                messageProducer,
                sagaLockManager);
    }

    public static CommandDispatcher getOrderCommandDispatcher() {
        OrderCommandHandler orderCommandHandler = SingletonServiceFactory.getBean(OrderCommandHandler.class);
        ChannelMapping channelMapping = SingletonServiceFactory.getBean(ChannelMapping.class);
        MessageProducer messageProducer = SingletonServiceFactory.getBean(MessageProducer.class);
        MessageConsumer messageConsumer = SingletonServiceFactory.getBean(MessageConsumer.class);
        SagaLockManager sagaLockManager = SingletonServiceFactory.getBean(SagaLockManager.class);
        return new SagaCommandDispatcher("orderCommandDispatcher",
                orderCommandHandler.commandHandlerDefinitions(),
                channelMapping,
                messageConsumer,
                messageProducer,
                sagaLockManager);

    }
}
