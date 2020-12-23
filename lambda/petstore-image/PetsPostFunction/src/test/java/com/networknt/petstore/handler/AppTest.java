
package com.networknt.petstore.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    ObjectMapper objectMapper = new ObjectMapper();

    public static void setEnv(String key, String value) {
        try {
            Map<String, String> env = System.getenv();
            Class<?> cl = env.getClass();
            Field field = cl.getDeclaredField("m");
            field.setAccessible(true);
            Map<String, String> writableEnv = (Map<String, String>) field.get(env);
            writableEnv.put(key, value);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to set environment variable", e);
        }
    }

    @Test
    @Disabled
    public void successResponse() {
        App app = new App();
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer ");
        request.setHeaders(headers);

        APIGatewayProxyRequestEvent.ProxyRequestContext context = new APIGatewayProxyRequestEvent.ProxyRequestContext();
        setEnv("AWS_REGION", "us-east-1");
        context.setAccountId("1234567890");
        context.setStage("Prod");
        context.setApiId("gy415nuibc");
        context.setHttpMethod("POST");
        APIGatewayProxyRequestEvent.RequestIdentity identity = new APIGatewayProxyRequestEvent.RequestIdentity();
        identity.setAccountId("1234567890");
        context.setIdentity(identity);
        request.setRequestContext(context);

        APIGatewayProxyResponseEvent result = app.handleRequest(request, null);
        assertEquals(result.getStatusCode().intValue(), 200);
        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
        String content = result.getBody();
        assertNotNull(content);
    }

    @Test
    @Disabled
    public void failureResponse() {
        App app = new App();
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer ");
        request.setHeaders(headers);

        APIGatewayProxyRequestEvent.ProxyRequestContext context = new APIGatewayProxyRequestEvent.ProxyRequestContext();
        setEnv("AWS_REGION", "us-east-1");
        context.setAccountId("1234567890");
        context.setStage("Prod");
        context.setApiId("gy415nuibc");
        context.setHttpMethod("POST");
        APIGatewayProxyRequestEvent.RequestIdentity identity = new APIGatewayProxyRequestEvent.RequestIdentity();
        identity.setAccountId("1234567890");
        context.setIdentity(identity);
        request.setRequestContext(context);

        APIGatewayProxyResponseEvent result = app.handleRequest(request, null);
        assertEquals(result.getStatusCode().intValue(), 400);
        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
        String content = result.getBody();
        assertNotNull(content);
    }
}
