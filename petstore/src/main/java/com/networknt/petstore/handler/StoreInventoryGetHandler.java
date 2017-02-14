package com.networknt.petstore.handler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;

public class StoreInventoryGetHandler implements HttpHandler {
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Map<String, Object> examples = new HashMap<>();
        examples.put("application/json", StringEscapeUtils.unescapeHtml4("{  &quot;key&quot; : 123}"));
        if(examples.size() > 0) {
            exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
            exchange.getResponseSender().send((String)examples.get("application/json"));
        } else {
            exchange.endExchange();
        }
    }
}
