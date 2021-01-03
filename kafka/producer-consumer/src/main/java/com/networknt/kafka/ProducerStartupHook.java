package com.networknt.kafka;

import com.networknt.service.SingletonServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.networknt.kafka.producer.LightProducer;
import com.networknt.kafka.producer.TransactionalProducer;
import com.networknt.server.StartupHookProvider;

public class ProducerStartupHook implements StartupHookProvider {
    private static Logger logger = LoggerFactory.getLogger(ProducerStartupHook.class);
    @Override
    public void onStartup() {
        logger.debug("ProducerStartupHook start");
        TransactionalProducer producer = (TransactionalProducer) SingletonServiceFactory.getBean(LightProducer.class);
        producer.open();
        new Thread(producer).start();
        logger.debug("ProducerStartupHook complete");
    }
}
