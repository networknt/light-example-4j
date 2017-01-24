package com.networknt.database.handler;

import com.networknt.service.SingletonServiceFactory;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.sql.DataSource;

public class UpdatesGetHandler implements HttpHandler {
    private static final DataSource ds = (DataSource) SingletonServiceFactory.getBean(DataSource.class);

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Map<String, Object> examples = new HashMap<>();
        examples.put("application/json", StringEscapeUtils.unescapeHtml4("[ {  &quot;randomNumber&quot; : 123,  &quot;id&quot; : 123} ]"));
        if(examples.size() > 0) {
            exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
            exchange.getResponseSender().send((String)examples.get("application/json"));
        } else {
            exchange.endExchange();
        }
    }
}
