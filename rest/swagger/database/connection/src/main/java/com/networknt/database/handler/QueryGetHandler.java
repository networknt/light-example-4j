
package com.networknt.database.handler;

import com.networknt.service.SingletonServiceFactory;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class QueryGetHandler implements HttpHandler {
    private static final DataSource ds = SingletonServiceFactory.getBean(DataSource.class);

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        
            exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
             exchange.getResponseSender().send(" {\n                                \"id\": 123,\n                                \"randomNumber\": 456\n                            }");
        
    }
}
