
package com.networknt.example.pdf.handler;

import com.networknt.server.Server;
import com.networknt.server.ServerConfig;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class TestServer implements BeforeAllCallback, AfterAllCallback {
    static final Logger logger = LoggerFactory.getLogger(TestServer.class);

    private static final AtomicInteger refCount = new AtomicInteger(0);
    private static Server server;

    private static final TestServer instance = new TestServer();

    private TestServer() {
    }

    public static TestServer getInstance() {
        return instance;
    }

    public ServerConfig getServerConfig() {
        return Server.getServerConfig();
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        try {
            if (refCount.get() == 0) {
                Server.start();
            }
        } finally {
            refCount.getAndIncrement();
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        refCount.getAndDecrement();
        if (refCount.get() == 0) {
            Server.stop();
        }
    }
}
