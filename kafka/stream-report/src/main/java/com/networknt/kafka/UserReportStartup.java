package com.networknt.kafka;

import com.networknt.server.Server;
import com.networknt.server.StartupHookProvider;
import com.networknt.utility.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserReportStartup implements StartupHookProvider {
    static final Logger logger = LoggerFactory.getLogger(UserReportStartup.class);
    public static UserReportStreams streams = null;
    @Override
    public void onStartup() {
        int port = Server.getServerConfig().getHttpsPort();
        String ip = NetUtils.getLocalAddressByDatagram();
        logger.info("ip = " + ip + " port = " + port);
        streams = new UserReportStreams();
        // start the kafka stream process
        streams.start(ip, port);
    }
}
