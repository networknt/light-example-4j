package com.networknt.kafka.handler;

import com.networknt.body.BodyHandler;
import com.networknt.config.JsonMapper;
import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class KsqldbPostHandler implements LightHttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(KsqldbPostHandler.class);

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        List<String> body = (List<String>)exchange.getAttachment(BodyHandler.REQUEST_BODY);
        logger.info("received call from the sidecar: " + JsonMapper.toJson(body));
        exchange.setStatusCode(204);
        exchange.endExchange();
    }
}
