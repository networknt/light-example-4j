
package com.networknt.backend.handler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.HashMap;
import java.util.Map;

public class GetDataGetHandler implements HttpHandler {
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Map<String, Object> examples = new HashMap<>();
        examples.put("application/json", StringEscapeUtils.unescapeHtml4("[ {  &quot;value&quot; : &quot;aeiou&quot;,  &quot;key&quot; : &quot;aeiou&quot;} ]"));
        if(examples.size() > 0) {
            exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
            exchange.getResponseSender().send((String)examples.get("application/json"));
        } else {
            exchange.endExchange();
        }
    }
}
