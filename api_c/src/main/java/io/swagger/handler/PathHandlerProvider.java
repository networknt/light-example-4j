package io.swagger.handler;

import com.networknt.config.Config;
import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;

import java.util.ArrayList;
import java.util.List;

public class PathHandlerProvider implements HandlerProvider {

    public HttpHandler getHandler() {
        HttpHandler handler = Handlers.routing()


            .add(Methods.GET, "/v1/data", new HttpHandler() {
                        public void handleRequest(HttpServerExchange exchange) throws Exception {
                            List<String> messages = new ArrayList<String>();
                            messages.add("API C: Message 1");
                            messages.add("API C: Message 2");
                            exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(messages));
                        }
                    })

        ;
        return handler;
    }
}

