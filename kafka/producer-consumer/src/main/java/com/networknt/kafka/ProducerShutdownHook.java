package com.networknt.kafka;

import com.networknt.kafka.producer.LightProducer;
import com.networknt.server.ShutdownHookProvider;
import com.networknt.service.SingletonServiceFactory;

public class ProducerShutdownHook implements ShutdownHookProvider {
    @Override
    public void onShutdown() {
        LightProducer producer = SingletonServiceFactory.getBean(LightProducer.class);
        try { if(producer != null) producer.close(); } catch(Exception e) {e.printStackTrace();}
    }
}
