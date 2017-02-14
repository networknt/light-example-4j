package com.networknt.petstore.handler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;

public class StoreOrderOrderIdGetHandler implements HttpHandler {
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Map<String, Object> examples = new HashMap<>();
        examples.put("application/xml", StringEscapeUtils.unescapeHtml4("&lt;Order&gt;  &lt;id&gt;123456&lt;/id&gt;  &lt;petId&gt;123456&lt;/petId&gt;  &lt;quantity&gt;0&lt;/quantity&gt;  &lt;shipDate&gt;2000-01-23T04:56:07.000Z&lt;/shipDate&gt;  &lt;status&gt;string&lt;/status&gt;  &lt;complete&gt;true&lt;/complete&gt;&lt;/Order&gt;"));
        examples.put("application/json", StringEscapeUtils.unescapeHtml4("{  &quot;petId&quot; : 123456789,  &quot;quantity&quot; : 123,  &quot;id&quot; : 123456789,  &quot;shipDate&quot; : &quot;2000-01-23T04:56:07.000+00:00&quot;,  &quot;complete&quot; : true,  &quot;status&quot; : &quot;aeiou&quot;}"));
        if(examples.size() > 0) {
            exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
            exchange.getResponseSender().send((String)examples.get("application/json"));
        } else {
            exchange.endExchange();
        }
    }
}
