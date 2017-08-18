
package com.networknt.database.handler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.Map;
import com.networknt.service.SingletonServiceFactory;
import javax.sql.DataSource;

public class QueriesGetHandler implements HttpHandler {
    private static final DataSource ds = (DataSource) SingletonServiceFactory.getBean(DataSource.class);

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        
            exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
             exchange.getResponseSender().send(" [\n                                {\n                                    \"id\": 123,\n                                    \"randomNumber\": 456\n                                },\n                                {\n                                    \"id\": 567,\n                                    \"randomNumber\": 789\n                                }\n                            ]");
        
    }
}
