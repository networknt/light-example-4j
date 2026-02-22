
package com.networknt.petstore.handler;

import com.networknt.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.networknt.server.Server;
import com.networknt.server.ServerConfig;

public class TestServer {
    static final Logger logger = LoggerFactory.getLogger(TestServer.class);

    private static final AtomicInteger refCount = new AtomicInteger(0);
    private static Server server;

    private static final TestServer instance  = new TestServer();

    public static TestServer getInstance () {
        return instance;
    }

    private TestServer() {

    }

    public ServerConfig getServerConfig() {
        return Server.getServerConfig();
    }

    public void start() {
        try {
            if (refCount.get() == 0) {
                Server.start();
            }
        }
        finally {
            refCount.getAndIncrement();
        }
    }

    public void stop() {
        refCount.getAndDecrement();
        if (refCount.get() == 0) {
            Server.stop();
        }
    }
}
