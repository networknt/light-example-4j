package com.networknt.kafka;

import com.networknt.kafka.consumer.LightConsumer;
import com.networknt.server.ShutdownHookProvider;
import com.networknt.service.SingletonServiceFactory;

public class ConsumerShutdownHook implements ShutdownHookProvider {
    @Override
    public void onShutdown() {
        LightConsumer consumer = SingletonServiceFactory.getBean(LightConsumer.class);
        try { if(consumer != null) consumer.close(); } catch(Exception e) {e.printStackTrace();}
    }
}
