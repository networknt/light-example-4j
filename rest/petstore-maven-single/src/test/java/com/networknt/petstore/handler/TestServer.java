
package com.networknt.petstore.handler;

import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.networknt.server.Server;
import com.networknt.server.ServerConfig;

public class TestServer extends ExternalResource {
    static final Logger logger = LoggerFactory.getLogger(TestServer.class);

    private static final AtomicInteger refCount = new AtomicInteger(0);

    private static final TestServer instance  = new TestServer();

    private TestServer() {
        logger.info("TestServer is constructed!");
    }

    public static TestServer getInstance () {
        return instance;
    }


    public ServerConfig getServerConfig() {
        return Server.getServerConfig();
    }

    @Override
    protected void before() {
        try {
            if (refCount.get() == 0) {
                Server.init();
                logger.info("TestServer is initialized!");
            }
        }
        finally {
            refCount.getAndIncrement();
        }
    }

    @Override
    protected void after() {
        refCount.getAndDecrement();
        if (refCount.get() == 0) {
            Server.stop();
            logger.info("TestServer is stopped!");
        }
    }
}
