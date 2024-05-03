
package com.networknt.market.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.networknt.utility.Constants;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BusinessHandlerTest extends AppTest {

    @Test
    @Disabled
    public void successResponse() {
        App app = new App();
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        Map<String, String> headers = new HashMap<>();
        // when running unit tests, there is no gateway authorizer and the token is not used.
        headers.put("Authorization", "Bearer ");
        request.setHeaders(headers);
        request.setPath("/{store}/products");
        request.setHttpMethod("GET");
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
        Map<String, Object> authorizerMap = new HashMap<>();
        // Simulate the authorizer to manually inject the primary token scopes for the scope verifier.
        authorizerMap.put(Constants.PRIMARY_SCOPES, "read:products");
        context.setAuthorizer(authorizerMap);
        APIGatewayProxyResponseEvent result = app.handleRequest(request, null);
        System.out.println(result.getBody());
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
        // when running unit tests, there is no gateway authorizer and the token is not used.
        headers.put("Authorization", "Bearer ");
        request.setHeaders(headers);
        request.setPath("/{store}/products");
        request.setHttpMethod("GET");
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
        Map<String, Object> authorizerMap = new HashMap<>();
        // Simulate the authorizer to manually inject the primary token scopes for the scope verifier.
        authorizerMap.put(Constants.PRIMARY_SCOPES, "read:products");
        context.setAuthorizer(authorizerMap);
        APIGatewayProxyResponseEvent result = app.handleRequest(request, null);
        System.out.println(result.getBody());
        assertEquals(result.getStatusCode().intValue(), 400);
        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
        String content = result.getBody();
        assertNotNull(content);
    }
}
