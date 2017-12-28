/**
 * 
 */
package com.networknt.tram.todolist.startup;


import com.networknt.server.StartupHookProvider;
import com.networknt.service.SingletonServiceFactory;
import com.networknt.tram.event.subscriber.DomainEventDispatcher;
import com.networknt.tram.message.consumer.MessageConsumer;
import com.networknt.tram.todolist.view.TodoEventConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StartUpTramMessageDispacher implements StartupHookProvider {

	static final Logger logger = LoggerFactory.getLogger(StartUpTramMessageDispacher.class);
	public static  DomainEventDispatcher domainEventDispatcher;

	@Override
	public void onStartup() {
		MessageConsumer messageConsumer =  (MessageConsumer) SingletonServiceFactory.getBean(MessageConsumer.class);
		TodoEventConsumer todoEventConsumer =  (TodoEventConsumer) SingletonServiceFactory.getBean(TodoEventConsumer.class);
		domainEventDispatcher = new DomainEventDispatcher("todoServiceEvents", todoEventConsumer.domainEventHandlers(), messageConsumer);
		domainEventDispatcher.initialize();
	}
}
