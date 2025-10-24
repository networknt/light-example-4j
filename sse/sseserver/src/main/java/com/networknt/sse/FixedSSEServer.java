package com.networknt.sse;

import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;

public class FixedSSEServer {

    public static void main(String[] args) {
        RoutingHandler router = new RoutingHandler()
                //.get("/", new HtmlClientHandler())
                //.get("/sse", new BackgroundSSEHandler())
                //.get("/sse", new RobustSSEHandler())
                //.get("/sse", new SimpleSSEHandler())
                //.get("/sse", new DebugSSEHandler())
                //.get("/sse", new FixedSSEHandler())
                //.get("/sse", new AsyncSSEHandler())
                //.get("/sse", new SimpleWorkingSSEHandler())
                .get("/sse", new WorkingSSEHandler())
                .get("/health", exchange -> {
                    exchange.getResponseSender().send("Server is running!");
                });

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(router)
                .build();

        server.start();
        System.out.println("Fixed SSE Server started on http://localhost:8080");

        // Add shutdown hook for graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server...");
            server.stop();
        }));
    }
}
