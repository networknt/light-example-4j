package com.networknt.ab.handler;

import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataGetHandler implements LightHttpHandler {
    static Logger logger = LoggerFactory.getLogger(DataGetHandler.class);

    public DataGetHandler() {
        if(logger.isInfoEnabled()) logger.info("DataGetHandler is constructed!");
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseSender().send("{\"key1\":\"value1\"}");
    }
}
