/**
 * 
 */
package com.networknt.saga.example.order.service.startup;

import com.networknt.example.sagas.ordersandcustomers.order.service.OrderCommandHandler;
import com.networknt.saga.participant.SagaCommandDispatcher;
import com.networknt.saga.participant.SagaLockManager;
import com.networknt.server.StartupHookProvider;
import com.networknt.service.SingletonServiceFactory;
import com.networknt.tram.command.common.ChannelMapping;
import com.networknt.tram.message.consumer.MessageConsumer;
import com.networknt.tram.message.producer.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartUpOrderCommandDispacher implements StartupHookProvider {

	static final Logger logger = LoggerFactory.getLogger(StartUpOrderCommandDispacher.class);
	@Override
	public void onStartup() {

		OrderCommandHandler orderCommandHandler = SingletonServiceFactory.getBean(OrderCommandHandler.class);
		ChannelMapping channelMapping = SingletonServiceFactory.getBean(ChannelMapping.class);
		MessageProducer messageProducer = SingletonServiceFactory.getBean(MessageProducer.class);
		MessageConsumer messageConsumer = SingletonServiceFactory.getBean(MessageConsumer.class);
		SagaLockManager sagaLockManager = SingletonServiceFactory.getBean(SagaLockManager.class);
		new SagaCommandDispatcher("orderCommandDispatcher",
				orderCommandHandler.commandHandlerDefinitions(),
				channelMapping,
				messageConsumer,
				messageProducer,
				sagaLockManager).initialize();
	}
}
