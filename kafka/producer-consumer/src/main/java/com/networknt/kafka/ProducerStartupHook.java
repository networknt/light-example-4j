package com.networknt.kafka;

import com.networknt.service.SingletonServiceFactory;
import org.apache.kafka.common.config.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.networknt.kafka.producer.LightProducer;
import com.networknt.kafka.producer.TransactionalProducer;
import com.networknt.server.StartupHookProvider;

import java.util.concurrent.atomic.AtomicReference;

public class ProducerStartupHook implements StartupHookProvider {
    private static Logger logger = LoggerFactory.getLogger(ProducerStartupHook.class);
    @Override
    public void onStartup() {
        logger.debug("ProducerStartupHook start");
        TransactionalProducer producer = (TransactionalProducer) SingletonServiceFactory.getBean(LightProducer.class);
        producer.open();
        Thread thread = new Thread(producer);
        final AtomicReference throwableReference = new AtomicReference<Throwable>();
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                throwableReference.set(e);
            }
        });
        try {
            thread.start();
            thread.join(1000);
            Throwable throwable = (Throwable) throwableReference.get();
            if (throwable != null) {
                if (throwable instanceof ConfigException) {
                    throw (ConfigException)throwable;
                } else if (throwable instanceof RuntimeException) {
                    throw (RuntimeException)throwable;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug("ProducerStartupHook complete");
    }
}
