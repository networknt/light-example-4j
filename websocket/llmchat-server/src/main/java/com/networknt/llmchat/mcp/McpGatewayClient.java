package com.networknt.llmchat.mcp;

import com.networknt.config.JsonMapper;
import com.networknt.utility.StringUtils;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class McpGatewayClient {
    private static final Logger logger = LoggerFactory.getLogger(McpGatewayClient.class);
    private final McpClientConfig config;
    private final HttpClient client;

    public McpGatewayClient() {
        this(McpClientConfig.load());
    }

    McpGatewayClient(McpClientConfig config) {
        this.config = config;
        this.client = buildClient(config);
    }

    public boolean isEnabled() {
        return config != null
                && config.isEnabled()
                && !StringUtils.isBlank(config.getGatewayUrl())
                && !StringUtils.isBlank(config.getPath());
    }

    public List<McpToolDefinition> listTools(String authorizationHeader) {
        if (!isEnabled() || StringUtils.isBlank(authorizationHeader)) {
            return Collections.emptyList();
        }
        Map<String, Object> response = post(authorizationHeader, "tools/list", Collections.emptyMap());
        Object resultObject = response.get("result");
        if (!(resultObject instanceof Map)) {
            return Collections.emptyList();
        }
        Map<?, ?> result = (Map<?, ?>) resultObject;
        Object toolsObject = result.get("tools");
        if (!(toolsObject instanceof List)) {
            return Collections.emptyList();
        }
        List<?> tools = (List<?>) toolsObject;

        List<McpToolDefinition> definitions = new ArrayList<>();
        for (Object toolObject : tools) {
            if (toolObject instanceof Map) {
                Map<?, ?> tool = (Map<?, ?>) toolObject;
                Object name = tool.get("name");
                if (name instanceof String && !((String) name).trim().isEmpty()) {
                    String toolName = (String) name;
                    String description = tool.get("description") instanceof String ? (String) tool.get("description") : "";
                    Map<String, Object> inputSchema = tool.get("inputSchema") instanceof Map
                            ? (Map<String, Object>) tool.get("inputSchema")
                            : Collections.emptyMap();
                    definitions.add(new McpToolDefinition(toolName, description, inputSchema));
                }
            }
        }
        return definitions;
    }

    public String callTool(String authorizationHeader, String toolName, String argumentsJson) {
        if (!isEnabled()) {
            throw new IllegalStateException("MCP client is disabled");
        }
        if (StringUtils.isBlank(authorizationHeader)) {
            throw new IllegalStateException("Missing Authorization header for MCP tool call");
        }
        Map<String, Object> arguments = Collections.emptyMap();
        if (!StringUtils.isBlank(argumentsJson)) {
            arguments = JsonMapper.fromJson(argumentsJson, Map.class);
        }
        Map<String, Object> response = post(
                authorizationHeader,
                "tools/call",
                Map.of("name", toolName, "arguments", arguments));

        Object resultObject = response.get("result");
        if (resultObject == null) {
            return "";
        }
        if (resultObject instanceof String) {
            return (String) resultObject;
        }
        if (resultObject instanceof Map) {
            Map<?, ?> resultMap = (Map<?, ?>) resultObject;
            Object content = resultMap.get("content");
            if (content instanceof List) {
                List<?> contentItems = (List<?>) content;
                StringBuilder builder = new StringBuilder();
                for (Object item : contentItems) {
                    if (item instanceof Map) {
                        Map<?, ?> contentMap = (Map<?, ?>) item;
                        Object text = contentMap.get("text");
                        if (text instanceof String) {
                            String textValue = (String) text;
                            if (builder.length() > 0) {
                                builder.append('\n');
                            }
                            builder.append(textValue);
                        }
                    }
                }
                if (builder.length() > 0) {
                    return builder.toString();
                }
            }
        }
        return JsonMapper.toJson(resultObject);
    }

    private Map<String, Object> post(String authorizationHeader, String method, Map<String, Object> params) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("jsonrpc", "2.0");
            payload.put("method", method);
            payload.put("params", params);
            payload.put("id", UUID.randomUUID().toString());
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(buildUrl()))
                    .timeout(Duration.ofMillis(config.getTimeout() > 0 ? config.getTimeout() : 5000))
                    .header("Content-Type", "application/json")
                    .header("Authorization", authorizationHeader)
                    .POST(HttpRequest.BodyPublishers.ofString(JsonMapper.toJson(payload)))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException("MCP gateway returned status " + response.statusCode() + ": " + response.body());
            }
            Map<String, Object> body = JsonMapper.fromJson(response.body(), Map.class);
            Object errorObject = body.get("error");
            if (errorObject instanceof Map) {
                Map<?, ?> error = (Map<?, ?>) errorObject;
                Object message = error.get("message");
                throw new IllegalStateException(message instanceof String ? (String) message : JsonMapper.toJson(errorObject));
            }
            return body;
        } catch (Exception e) {
            logger.error("Failed to invoke MCP gateway method {}", method, e);
            throw new RuntimeException("Failed to invoke MCP gateway method " + method + ": " + e.getMessage(), e);
        }
    }

    private String buildUrl() {
        String gatewayUrl = config.getGatewayUrl();
        String path = config.getPath();
        if (gatewayUrl.endsWith("/") && path.startsWith("/")) {
            return gatewayUrl.substring(0, gatewayUrl.length() - 1) + path;
        }
        if (!gatewayUrl.endsWith("/") && !path.startsWith("/")) {
            return gatewayUrl + "/" + path;
        }
        return gatewayUrl + path;
    }

    private static java.net.http.HttpClient buildClient(McpClientConfig config) {
        java.net.http.HttpClient.Builder builder = java.net.http.HttpClient.newBuilder();
        if (config != null && !config.isVerifyHostname()) {
            try {
                TrustManager[] trustAll = new TrustManager[] {new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }};
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustAll, new SecureRandom());
                SSLParameters sslParameters = new SSLParameters();
                sslParameters.setEndpointIdentificationAlgorithm("");
                builder.sslContext(sslContext).sslParameters(sslParameters);
            } catch (Exception e) {
                throw new RuntimeException("Unable to initialize insecure TLS HTTP client", e);
            }
        }
        return builder.build();
    }

    public static class McpToolDefinition {
        private final String name;
        private final String description;
        private final Map<String, Object> inputSchema;

        public McpToolDefinition(String name, String description, Map<String, Object> inputSchema) {
            this.name = name;
            this.description = description;
            this.inputSchema = inputSchema;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Map<String, Object> getInputSchema() {
            return inputSchema;
        }
    }
}
