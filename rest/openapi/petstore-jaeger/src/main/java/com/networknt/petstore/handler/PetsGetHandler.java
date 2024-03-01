package com.networknt.petstore.handler;

import com.networknt.handler.LightHttpHandler;
import com.networknt.jaeger.tracing.JaegerHandler;
import com.networknt.jaeger.tracing.JaegerStartupHookProvider;
import io.opentracing.Span;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.Map;

public class PetsGetHandler implements LightHttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Span span = JaegerStartupHookProvider.tracer.buildSpan("PetsGetHandler").asChildOf(exchange.getAttachment(JaegerHandler.ROOT_SPAN)).start();
        try {
            exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
            exchange.setStatusCode(200);
            exchange.getResponseSender().send("[{\"id\":1,\"name\":\"catten\",\"tag\":\"cat\"},{\"id\":2,\"name\":\"doggy\",\"tag\":\"dog\"}]");
        } finally {
            span.finish();
        }
    }
}
