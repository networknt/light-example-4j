package com.networknt.kafka;

import com.networknt.server.ShutdownHookProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the class to be called during the light-4j server shutdown to release resources. It needs
 * to be added to the service.yml file.
 *
 */
public class WordCountShutdownHook implements ShutdownHookProvider {
    private static final Logger logger = LoggerFactory.getLogger(WordCountShutdownHook.class);

    @Override
    public void onShutdown() {
        logger.info("WordCountShutdownHook onShutdown begins.");
        if(WordCountStartupHook.streams != null) {
            WordCountStartupHook.streams.close();
        }
        logger.info("WordCountShutdownHook onShutdown ends.");
    }
}
