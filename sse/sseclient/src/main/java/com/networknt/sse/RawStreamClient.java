package com.networknt.sse;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RawStreamClient {

    public static void main(String[] args) {
        System.out.println("--- Raw Stream Client ---");

        try {
            URL url = new URL("http://localhost:8080/sse");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "text/event-stream");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(30000); // 30 second timeout

            System.out.println("Connecting...");
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            System.out.println("Content-Type: " + connection.getContentType());
            System.out.println("=== RAW STREAM START ===");

            InputStream stream = connection.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            long totalBytes = 0;
            long startTime = System.currentTimeMillis();

            while ((bytesRead = stream.read(buffer)) != -1) {
                totalBytes += bytesRead;
                String chunk = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                System.out.print(chunk);

                // Stop after 30 seconds or if we got some data
                if (System.currentTimeMillis() - startTime > 30000) {
                    System.out.println("\n=== Timeout after 30 seconds ===");
                    break;
                }

                // If we received data, flush output
                System.out.flush();
            }

            System.out.println("\n=== RAW STREAM END ===");
            System.out.println("Total bytes received: " + totalBytes);
            System.out.println("Connection closed by: " + (totalBytes > 0 ? "Server" : "Client/Timeout"));

            connection.disconnect();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
