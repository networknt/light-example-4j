package com.networknt.kafka;

import com.networknt.kafka.consumer.LightConsumer;
import com.networknt.server.StartupHookProvider;
import com.networknt.service.SingletonServiceFactory;

public class ConsumerStartupHook implements StartupHookProvider {
    public static UserConsumer consumer;
    @Override
    public void onStartup() {
        consumer = (UserConsumer) SingletonServiceFactory.getBean(LightConsumer.class);
        consumer.open();
    }
}
