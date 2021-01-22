package com.networknt.kafka.handler;

import com.networknt.client.Http2Client;
import com.networknt.handler.LightHttpHandler;
import io.undertow.client.ClientConnection;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class ReportGetHandler implements LightHttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(ReportGetHandler.class);

    private static final String OBJECT_NOT_FOUND = "ERR11637";
    private static final String GENERIC_EXCEPTION = "ERR10014";

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

    }
}
