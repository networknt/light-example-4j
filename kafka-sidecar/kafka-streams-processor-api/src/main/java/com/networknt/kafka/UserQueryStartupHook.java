package com.networknt.kafka;

import com.networknt.server.Server;
import com.networknt.server.StartupHookProvider;
import com.networknt.utility.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserQueryStartupHook implements StartupHookProvider {
    static final Logger logger = LoggerFactory.getLogger(UserQueryStartupHook.class);
    public static UserQueryStreams streams = null;
    @Override
    public void onStartup() {
        int port = Server.getServerConfig().getHttpsPort();
        String ip = NetUtils.getLocalAddressByDatagram();
        logger.info("ip = " + ip + " port = " + port);
        streams = new UserQueryStreams();
        // start the kafka stream process
        streams.start(ip, port);
    }
}
