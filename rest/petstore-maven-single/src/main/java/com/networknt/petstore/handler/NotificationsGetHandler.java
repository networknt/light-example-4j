package com.networknt.petstore.handler;

import com.networknt.body.BodyHandler;
import com.networknt.config.Config;
import com.networknt.handler.LightHttpHandler;
import com.networknt.http.*;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;

import java.util.Deque;
import java.util.Map;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class NotificationsGetHandler implements LightHttpHandler {

    public NotificationsGetHandler () {
    }


    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = "{\"data\":null,\"notifications\":[{\"code\":\"ERR00610000\",\"message\":\"Exception in getting service:Unable to create user info\",\"timestamp\":1655739885937,\"metadata\":null,\"description\":\"Internal Server Error\"}]}";
        exchange.setStatusCode(HttpStatus.OK.value());
        exchange.getResponseSender().send(body);
    }
}
