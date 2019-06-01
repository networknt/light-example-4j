package com.networknt.database.handler;

import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.Map;
import com.networknt.service.SingletonServiceFactory;
import javax.sql.DataSource;

public class QueryGetHandler implements LightHttpHandler {

    private static final DataSource ds = SingletonServiceFactory.getBean(DataSource.class);
    
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.setStatusCode(200);
        exchange.getResponseSender().send("{\"id\":123,\"randomNumber\":456}");
    }
}
