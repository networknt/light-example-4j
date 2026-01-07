package com.networknt.aa.handler;

import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DataGetHandler implements LightHttpHandler {
    private HttpClient client;

    public DataGetHandler() {
        if(logger.isInfoEnabled()) logger.info("DataGetHandler is constructed!");
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        client = createJavaHttpClient();
        exchange.dispatch(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:7002/v1/data"))
                        .GET()
                        .header("Accept", "application/json")
                        .build();

                HttpResponse<String> response =
                        client.send(request, HttpResponse.BodyHandlers.ofString());

                exchange.setStatusCode(response.statusCode());
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                exchange.getResponseSender().send(response.body());

            } catch (Exception e) {
                logger.error("Error invoking /v1/data", e);
                exchange.setStatusCode(500);
                exchange.getResponseSender().send("Failed to call downstream service");
            }
        });
    }

    private HttpClient createJavaHttpClient() {
        var clientBuilder = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .version(HttpClient.Version.HTTP_1_1);

        return clientBuilder.build();
    }

}
