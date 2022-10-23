
package com.networknt.petstore.handler;

import com.networknt.server.Server;
import com.networknt.server.ServerConfig;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class TestServer implements BeforeAllCallback, AfterAllCallback {
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
    public void beforeAll(ExtensionContext context) throws Exception {
        try {
            if (refCount.get() == 0) {
                Server.start();
            }
        }
        finally {
            refCount.getAndIncrement();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> clean()));
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        clean();
    }

    protected void clean() {
        refCount.getAndDecrement();
        if (refCount.get() == 0) {
            Server.stop();
        }


    }
}
