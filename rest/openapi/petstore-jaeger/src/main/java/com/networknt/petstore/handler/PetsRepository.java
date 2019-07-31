package com.networknt.petstore.handler;

import com.networknt.jaeger.tracing.JaegerStartupHookProvider;
import io.opentracing.Scope;
import io.opentracing.Span;

public class PetsRepository {
    public static String getPetById(String id) {
        Span span = JaegerStartupHookProvider.tracer.buildSpan("getPetByIdFromDb").asChildOf(JaegerStartupHookProvider.tracer.activeSpan()).start();
        try {
            try {Thread.sleep(10); } catch (InterruptedException e) {}
            span.log("database call returned");
            return "{\"id\":1,\"name\":\"Jessica Right\",\"tag\":\"pet\"}";
        } finally {
            span.finish();
        }
    }
}
