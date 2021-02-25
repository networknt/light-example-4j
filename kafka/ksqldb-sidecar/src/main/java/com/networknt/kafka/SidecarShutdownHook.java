package com.networknt.kafka;

import com.networknt.server.ShutdownHookProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SidecarShutdownHook implements ShutdownHookProvider {
    private static final Logger logger = LoggerFactory.getLogger(SidecarShutdownHook.class);

    @Override
    public void onShutdown() {
        logger.info("SidecarShutdownHook is called");
        SidecarStartupHook.client.close();
    }
}
