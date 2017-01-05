package io.swagger.handler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;

public class UserUsernameGetHandler implements HttpHandler {
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Map<String, Object> examples = new HashMap<>();
        examples.put("application/xml", StringEscapeUtils.unescapeHtml4("&lt;User&gt;  &lt;id&gt;123456&lt;/id&gt;  &lt;username&gt;string&lt;/username&gt;  &lt;firstName&gt;string&lt;/firstName&gt;  &lt;lastName&gt;string&lt;/lastName&gt;  &lt;email&gt;string&lt;/email&gt;  &lt;password&gt;string&lt;/password&gt;  &lt;phone&gt;string&lt;/phone&gt;  &lt;userStatus&gt;0&lt;/userStatus&gt;&lt;/User&gt;"));
        examples.put("application/json", StringEscapeUtils.unescapeHtml4("{  &quot;firstName&quot; : &quot;aeiou&quot;,  &quot;lastName&quot; : &quot;aeiou&quot;,  &quot;password&quot; : &quot;aeiou&quot;,  &quot;userStatus&quot; : 123,  &quot;phone&quot; : &quot;aeiou&quot;,  &quot;id&quot; : 123456789,  &quot;email&quot; : &quot;aeiou&quot;,  &quot;username&quot; : &quot;aeiou&quot;}"));
        if(examples.size() > 0) {
            exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
            exchange.getResponseSender().send((String)examples.get("application/json"));
        } else {
            exchange.endExchange();
        }
    }
}
