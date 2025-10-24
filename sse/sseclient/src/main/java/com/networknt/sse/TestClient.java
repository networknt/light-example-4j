package com.networknt.sse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TestClient {
    public static void main(String[] args) throws Exception {
        URL url = new URL("http://localhost:8080/sse");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "text/event-stream");
        conn.setRequestProperty("Cache-Control", "no-cache");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(30000); // 30 seconds

        System.out.println("Connecting...");
        int code = conn.getResponseCode();
        System.out.println("Response: " + code);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("RECEIVED: " + line);
            }
        }

        System.out.println("Connection closed");
    }
}
