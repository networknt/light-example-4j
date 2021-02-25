package com.networknt.kafka.handler;

import com.networknt.handler.LightHttpHandler;
import io.confluent.ksql.api.client.*;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class QueryUserIdGetHandler implements LightHttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(QueryUserIdGetHandler.class);

    public QueryUserIdGetHandler() {
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseSender().send("{}");
    }
}
