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
    private static final String QUERY = "SELECT * from USER_TABLE WHERE USERID = '%s' EMIT CHANGES;";
    private static final String OBJECT_NOT_FOUND = "ERR11637";
    private static String KSQLDB_SERVER_HOST = "localhost";
    private static int KSQLDB_SERVER_HOST_PORT = 8088;
    public Client client = null;

    public QueryUserIdGetHandler() {
        ClientOptions options = ClientOptions.create()
                .setHost(KSQLDB_SERVER_HOST)
                .setPort(KSQLDB_SERVER_HOST_PORT);
        client = Client.create(options);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String userId = exchange.getQueryParameters().get("userId").getFirst();
        if(logger.isTraceEnabled()) logger.trace("userId = " + userId);
        String s = String.format(QUERY, userId);
        if(logger.isTraceEnabled()) logger.trace("query = " + s);
        Map<String, Object> properties = new HashMap<>();
        properties.put("auto.offset.reset", "earliest");
        StreamedQueryResult result = client.streamQuery(s, properties).get();
        Row row = result.poll();
        if(row != null) {
            exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "application/json");
            KsqlArray a = row.values();
            exchange.getResponseSender().send(a.toJsonString());
        } else {
            setExchangeStatus(exchange, OBJECT_NOT_FOUND, "user", userId);
        }
    }
}
