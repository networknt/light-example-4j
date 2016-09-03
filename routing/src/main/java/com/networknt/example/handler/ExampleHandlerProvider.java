package com.networknt.example.handler;

import com.networknt.config.Config;
import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.predicate.Predicates;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.PathTemplateHandler;
import io.undertow.util.Methods;


public class ExampleHandlerProvider implements HandlerProvider {

    public HttpHandler getHandler() {
        HttpHandler handler = Handlers.routing()
                .add(Methods.GET, "/baz", new HttpHandler() {
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        exchange.getResponseSender().send("baz");
                    }
                })
                .add(Methods.GET, "/baz/{foo}", new HttpHandler() {
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        exchange.getResponseSender().send("baz-path" + exchange.getQueryParameters().get("foo"));
                    }
                })
                .add(Methods.GET, "/bar", new HttpHandler() {
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        exchange.getResponseSender().send("get bar");
                    }
                })
                .add(Methods.PUT, "/bar", new HttpHandler() {
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        exchange.getResponseSender().send("put bar");
                    }
                })
                .add(Methods.POST, "/bar", new HttpHandler() {
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        exchange.getResponseSender().send("post bar");
                    }
                })
                .add(Methods.DELETE, "/bar", new HttpHandler() {
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        exchange.getResponseSender().send("delete bar");
                    }
                })
                .add(Methods.GET, "/wild/{test}/*", new HttpHandler() {
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        exchange.getResponseSender().send("wild:" + exchange.getQueryParameters().get("test") + ":" + exchange.getQueryParameters().get("*"));
                    }
                })
                .add(Methods.GET, "/foo", new HttpHandler() {
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        exchange.getResponseSender().send("foo");
                    }
                })
                .add(Methods.GET, "/foo", Predicates.parse("contains[value=%{i,SomeHeader},search='special'] "), new HttpHandler() {
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        exchange.getResponseSender().send("special foo");
                    }
                })
                .add(Methods.POST, "/foo", new HttpHandler() {
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        exchange.getResponseSender().send("posted foo");
                    }
                })
                .add(Methods.GET, "/foo/{bar}", new HttpHandler() {
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        exchange.getResponseSender().send("foo-path" + exchange.getQueryParameters().get("bar"));
                    }
                });
        return handler;
    }
}
