
package com.networknt.postservice.handler;

import com.networknt.body.BodyHandler;
import com.networknt.config.Config;
import com.networknt.handler.LightHttpHandler;
import com.networknt.server.Server;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDataPostHandler implements LightHttpHandler {
    static final Logger logger = LoggerFactory.getLogger(PostDataPostHandler.class);
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        List<Map<String, Object>> body = (List<Map<String, Object>>)exchange.getAttachment(BodyHandler.REQUEST_BODY);
        String result = Config.getInstance().getMapper().writeValueAsString(body);
        if(logger.isDebugEnabled()) logger.debug(result);
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send(result);
    }
}
