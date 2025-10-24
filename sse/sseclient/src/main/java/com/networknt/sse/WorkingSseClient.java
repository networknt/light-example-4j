package com.networknt.sse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WorkingSseClient {

    public static void main(String[] args) {
        System.out.println("--- Working SSE Client Started ---");
        System.out.println("Connecting to: http://localhost:8080/sse");

        try {
            URL url = new URL("http://localhost:8080/sse");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set up SSE request
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "text/event-stream");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setConnectTimeout(50000);
            connection.setReadTimeout(0); // No timeout - wait indefinitely

            System.out.println("Making connection...");
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                System.out.println("✓ Successfully connected to SSE stream");
                System.out.println("Content-Type: " + connection.getContentType());
                System.out.println("--- Waiting for events (will wait up to 60 seconds) ---");

                // Process the stream
                processStream(connection);

            } else {
                System.err.println("✗ Failed to connect. HTTP response: " + responseCode);
                // Read error stream
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.err.println("Error response: " + line);
                    }
                }
            }

            connection.disconnect();

        } catch (Exception e) {
            System.err.println("✗ Error in SSE client: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("--- SSE Client Ended ---");
    }

    private static void processStream(HttpURLConnection connection) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            String currentEvent = null;
            StringBuilder dataBuilder = new StringBuilder();
            int eventCount = 0;
            long startTime = System.currentTimeMillis();
            final long MAX_RUNTIME = 60000; // 60 seconds max

            while ((line = reader.readLine()) != null) {
                // Check if we've been running for too long
                if (System.currentTimeMillis() - startTime > MAX_RUNTIME) {
                    System.out.println("Reached maximum runtime of 60 seconds, exiting...");
                    break;
                }

                // Debug: show raw lines
                System.out.println("RAW: '" + line + "'");

                if (line.startsWith("event:")) {
                    currentEvent = line.substring(6).trim();
                } else if (line.startsWith("data:")) {
                    if (dataBuilder.length() > 0) {
                        dataBuilder.append(" ");
                    }
                    dataBuilder.append(line.substring(5).trim());
                } else if (line.isEmpty()) {
                    // Empty line indicates end of event
                    if (dataBuilder.length() > 0) {
                        eventCount++;
                        String data = dataBuilder.toString();
                        printEvent(currentEvent, data, eventCount);

                        if ("close".equals(currentEvent)) {
                            System.out.println("✓ Server sent close event, ending connection.");
                            break;
                        }
                    }
                    // Reset for next event
                    currentEvent = null;
                    dataBuilder.setLength(0);
                }
            }

            System.out.println("✓ Stream ended. Received " + eventCount + " events total.");

        } catch (java.net.SocketTimeoutException e) {
            System.err.println("✗ Read timeout - no data received");
        } catch (Exception e) {
            System.err.println("✗ Stream error: " + e.getMessage());
        }
    }

    private static void printEvent(String eventType, String data, int count) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.printf("[%s] Event #%d: %-12s Data: %s%n",
                timestamp,
                count,
                eventType != null ? eventType : "message",
                data);
    }
}
