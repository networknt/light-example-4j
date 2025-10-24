package com.networknt.sse;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.util.concurrent.atomic.AtomicInteger;

public class WorkingSSEHandler implements HttpHandler {

    private final AtomicInteger clientCounter = new AtomicInteger(0);

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }

        int clientId = clientCounter.incrementAndGet();
        System.out.println("=== CLIENT " + clientId + " CONNECTED ===");

        // Set SSE headers
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/event-stream");
        exchange.getResponseHeaders().put(Headers.CACHE_CONTROL, "no-cache");
        exchange.getResponseHeaders().put(Headers.CONNECTION, "keep-alive");
        //exchange.getResponseHeaders().put(Headers.ACCESS_CONTROL_ALLOW_ORIGIN, "*");

        // CRITICAL: Start blocking mode to keep the exchange alive
        exchange.startBlocking();

        try {
            // Send initial message
            System.out.println("Sending welcome to client " + clientId);
            sendEventBlocking(exchange, "connected", "Client " + clientId + " connected");

            // Send multiple messages - this keeps the handler alive
            for (int i = 1; i <= 10; i++) {
                System.out.println("Waiting 2 seconds before message " + i);
                Thread.sleep(2000);

                // Check if client disconnected
                if (isClientDisconnected(exchange)) {
                    System.out.println("Client " + clientId + " disconnected during message " + i);
                    break;
                }

                System.out.println("Sending message " + i + " to client " + clientId);
                sendEventBlocking(exchange, "message", "Message " + i + " at " +
                        java.time.LocalTime.now());
            }

            // Send close event if still connected
            if (!isClientDisconnected(exchange)) {
                System.out.println("Sending close event to client " + clientId);
                sendEventBlocking(exchange, "close", "Session completed");

                // Small delay to ensure the close event is sent
                Thread.sleep(100);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted for client " + clientId);
        } catch (Exception e) {
            System.err.println("Error for client " + clientId + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("=== CLIENT " + clientId + " HANDLER COMPLETED ===\n");
        }
    }

    private void sendEventBlocking(HttpServerExchange exchange, String event, String data) {
        try {
            String message = "event: " + event + "\n" +
                    "data: " + data + "\n\n";
            exchange.getOutputStream().write(message.getBytes());
            exchange.getOutputStream().flush(); // Force send immediately
            System.out.println("Event '" + event + "' sent successfully");
        } catch (Exception e) {
            System.err.println("Failed to send event '" + event + "': " + e.getMessage());
            throw new RuntimeException("Send failed", e);
        }
    }

    private boolean isClientDisconnected(HttpServerExchange exchange) {
        try {
            // Try to write a zero-byte to test if connection is alive
            exchange.getOutputStream().write(new byte[0]);
            exchange.getOutputStream().flush();
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
