/**
 *
 */
package com.networknt.saga.example.customer.service.startup;

import com.networknt.example.sagas.ordersandcustomers.customer.service.CustomerCommandHandler;
import com.networknt.example.sagas.ordersandcustomers.data.TramCommandsAndEventsIntegrationData;
import com.networknt.saga.participant.SagaCommandDispatcher;
import com.networknt.saga.participant.SagaLockManager;
import com.networknt.server.StartupHookProvider;
import com.networknt.service.SingletonServiceFactory;
import com.networknt.tram.command.common.ChannelMapping;
import com.networknt.tram.command.common.DefaultChannelMapping;
import com.networknt.tram.message.consumer.MessageConsumer;
import com.networknt.tram.message.producer.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartUpCustomerCommandDispacher implements StartupHookProvider {

	static final Logger logger = LoggerFactory.getLogger(StartUpCustomerCommandDispacher.class);
	@Override
	public void onStartup() {

		CustomerCommandHandler customerCommandHandler = SingletonServiceFactory.getBean(CustomerCommandHandler.class);

		TramCommandsAndEventsIntegrationData data = SingletonServiceFactory.getBean(TramCommandsAndEventsIntegrationData.class);

		ChannelMapping channelMapping =  DefaultChannelMapping.builder()
				.with("CustomerAggregate", data.getAggregateDestination())
				.with("customerService", data.getCommandChannel())
				.build();

		MessageProducer messageProducer =  SingletonServiceFactory.getBean(MessageProducer.class);
		MessageConsumer messageConsumer =  SingletonServiceFactory.getBean(MessageConsumer.class);
		SagaLockManager sagaLockManager = SingletonServiceFactory.getBean(SagaLockManager.class);
		new SagaCommandDispatcher("customerCommandDispatcher",
				customerCommandHandler.commandHandlerDefinitions(),
				channelMapping,
				messageConsumer,
				messageProducer,
				sagaLockManager).initialize();
	}
}
