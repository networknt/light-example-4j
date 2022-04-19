package com.networknt.kafka;

import com.networknt.server.Server;
import com.networknt.server.StartupHookProvider;
import com.networknt.utility.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class needs to be setup in the service.yml file to be called when light-4j server starts up.
 *
 */
public class WordCountStartupHook implements StartupHookProvider {
    static final Logger logger = LoggerFactory.getLogger(WordCountStartupHook.class);
    public static WordCountStreams streams = null;
    @Override
    public void onStartup() {
        logger.info("WordCountStartupHook onStartup begins.");
        int port = Server.getServerConfig().getHttpsPort();
        String ip = NetUtils.getLocalAddressByDatagram();
        logger.info("ip = " + ip + " port = " + port);
        streams = new WordCountStreams();
        // start the kafka stream process
        streams.start(ip, port);
        logger.info("WordCountStartupHook onStartup ends.");
    }
}
