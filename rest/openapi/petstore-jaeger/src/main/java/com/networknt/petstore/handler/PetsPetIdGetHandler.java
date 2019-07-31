package com.networknt.petstore.handler;

import com.networknt.handler.LightHttpHandler;
import com.networknt.jaeger.tracing.JaegerHandler;
import com.networknt.jaeger.tracing.JaegerStartupHookProvider;
import io.opentracing.Span;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.Map;

public class PetsPetIdGetHandler implements LightHttpHandler {
    
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Span span = JaegerStartupHookProvider.tracer.buildSpan("PetsPetIdGetHandler").asChildOf(exchange.getAttachment(JaegerHandler.ROOT_SPAN)).start();
        JaegerStartupHookProvider.tracer.scopeManager().activate(span);
        try  {
            String petId = exchange.getPathParameters().get("petId").getFirst();
            span.setTag("petId", petId);
            String result = PetsRepository.getPetById(petId);
            exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
            exchange.setStatusCode(200);
            exchange.getResponseSender().send(result);
        } catch (Exception e) {
            span.log(e.getMessage());
        } finally {
            span.finish();
        }
    }
}
