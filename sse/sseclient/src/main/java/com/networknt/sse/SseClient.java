package com.networknt.sse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SseClient {

    public static void main(String[] args) {
        System.out.println("--- SSE Client Started ---");

        try {
            URL url = new URL("http://localhost:8080/sse");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set up SSE request
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "text/event-stream");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(0); // No read timeout - wait indefinitely

            System.out.println("Connecting to SSE stream...");
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                System.out.println("Successfully connected to SSE stream");

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {

                    String line;
                    String currentEvent = null;
                    String currentId = null;
                    StringBuilder dataBuilder = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        System.out.println("DEBUG - Raw line: '" + line + "'"); // Debug output

                        if (line.startsWith("event:")) {
                            currentEvent = line.substring(6).trim();
                        } else if (line.startsWith("id:")) {
                            currentId = line.substring(3).trim();
                        } else if (line.startsWith("data:")) {
                            // Append data (handle multi-line data)
                            if (dataBuilder.length() > 0) {
                                dataBuilder.append("\n");
                            }
                            dataBuilder.append(line.substring(5).trim());
                        } else if (line.isEmpty()) {
                            // Empty line indicates end of event
                            if (dataBuilder.length() > 0) {
                                String data = dataBuilder.toString();
                                printEvent(currentEvent, currentId, data);

                                if ("close".equals(currentEvent)) {
                                    System.out.println("Server sent close event, ending connection.");
                                    break;
                                }
                            }
                            // Reset for next event
                            currentEvent = null;
                            currentId = null;
                            dataBuilder.setLength(0);
                        }
                        // Ignore other lines (like retry:)
                    }
                }
            } else {
                System.err.println("Failed to connect. HTTP response: " + responseCode);
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
            System.err.println("Error in SSE client: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("--- SSE Client Ended ---");
    }

    private static void printEvent(String eventType, String eventId, String data) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.printf("[%s] Event: %s, ID: %s, Data: %s%n",
                timestamp,
                eventType != null ? eventType : "message",
                eventId != null ? eventId : "N/A",
                data);
    }
}
