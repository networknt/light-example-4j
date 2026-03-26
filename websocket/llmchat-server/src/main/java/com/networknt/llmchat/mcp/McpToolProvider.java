package com.networknt.llmchat.mcp;

import com.networknt.genai.model.chat.request.json.JsonArraySchema;
import com.networknt.genai.model.chat.request.json.JsonBooleanSchema;
import com.networknt.genai.model.chat.request.json.JsonIntegerSchema;
import com.networknt.genai.model.chat.request.json.JsonNumberSchema;
import com.networknt.genai.model.chat.request.json.JsonObjectSchema;
import com.networknt.genai.model.chat.request.json.JsonSchemaElement;
import com.networknt.genai.model.chat.request.json.JsonStringSchema;
import com.networknt.genai.service.tool.ToolExecutor;
import com.networknt.genai.service.tool.ToolExecutionResult;
import com.networknt.genai.service.tool.ToolProvider;
import com.networknt.genai.service.tool.ToolProviderRequest;
import com.networknt.genai.service.tool.ToolProviderResult;
import com.networknt.genai.tool.ToolExecutionRequest;
import com.networknt.genai.tool.ToolSpecification;
import com.networknt.utility.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class McpToolProvider implements ToolProvider {
    private static final Logger logger = LoggerFactory.getLogger(McpToolProvider.class);
    private final McpGatewayClient client;

    public McpToolProvider() {
        this.client = new McpGatewayClient();
    }

    @Override
    public ToolProviderResult provideTools(ToolProviderRequest request) {
        if (!client.isEnabled()) {
            logger.info("McpToolProvider skipped because MCP client is disabled");
            return ToolProviderResult.builder().build();
        }

        String authorization = request.invocationParameters() == null
                ? null
                : request.invocationParameters().get("authorization");
        if (StringUtils.isBlank(authorization)) {
            logger.debug("Skipping MCP tool registration because Authorization is missing");
            return ToolProviderResult.builder().build();
        }

        logger.info("McpToolProvider invoked. authorizationPresent=true");
        ToolProviderResult.Builder builder = ToolProviderResult.builder();
        List<McpGatewayClient.McpToolDefinition> tools = client.listTools(authorization);
        logger.info("McpToolProvider received {} tools from MCP gateway", tools.size());
        for (McpGatewayClient.McpToolDefinition tool : tools) {
            builder.add(toToolSpecification(tool), toExecutor(tool.getName()));
        }
        return builder.build();
    }

    private ToolSpecification toToolSpecification(McpGatewayClient.McpToolDefinition tool) {
        return ToolSpecification.builder()
                .name(tool.getName())
                .description(tool.getDescription())
                .parameters(toObjectSchema(tool.getInputSchema()))
                .build();
    }

    private ToolExecutor toExecutor(String toolName) {
        return new ToolExecutor() {
            @Override
            public ToolExecutionResult executeWithContext(ToolExecutionRequest request, com.networknt.genai.invocation.InvocationContext context) {
                String authorization = context == null || context.invocationParameters() == null
                        ? null
                        : context.invocationParameters().get("authorization");
                String result = client.callTool(authorization, toolName, request.arguments());
                return ToolExecutionResult.builder()
                        .resultText(result)
                        .build();
            }

            @Override
            public String execute(ToolExecutionRequest request, Object memoryId) {
                return client.callTool(null, toolName, request.arguments());
            }
        };
    }

    private JsonObjectSchema toObjectSchema(Map<String, Object> schema) {
        JsonObjectSchema.Builder builder = JsonObjectSchema.builder();
        if (schema == null || schema.isEmpty()) {
            return builder.build();
        }

        Object descriptionObject = schema.get("description");
        if (descriptionObject instanceof String) {
            builder.description((String) descriptionObject);
        }
        Object propertiesObject = schema.get("properties");
        if (propertiesObject instanceof Map) {
            Map<?, ?> properties = (Map<?, ?>) propertiesObject;
            for (Map.Entry<?, ?> entry : properties.entrySet()) {
                if (entry.getKey() instanceof String && entry.getValue() instanceof Map) {
                    String propertyName = (String) entry.getKey();
                    Map<String, Object> propertySchema = (Map<String, Object>) entry.getValue();
                    builder.addProperty(propertyName, toSchemaElement(propertySchema));
                }
            }
        }
        Object requiredObject = schema.get("required");
        if (requiredObject instanceof List) {
            List<?> requiredList = (List<?>) requiredObject;
            List<String> required = new ArrayList<>();
            for (Object item : requiredList) {
                if (item instanceof String) {
                    required.add((String) item);
                }
            }
            builder.required(required);
        }
        Object additionalProperties = schema.get("additionalProperties");
        if (additionalProperties instanceof Boolean) {
            builder.additionalProperties((Boolean) additionalProperties);
        }
        return builder.build();
    }

    private JsonSchemaElement toSchemaElement(Map<String, Object> schema) {
        String type = schema.get("type") instanceof String ? (String) schema.get("type") : null;
        String description = schema.get("description") instanceof String ? (String) schema.get("description") : null;

        if ("string".equals(type)) {
            return JsonStringSchema.builder().description(description).build();
        }
        if ("integer".equals(type)) {
            return JsonIntegerSchema.builder().description(description).build();
        }
        if ("number".equals(type)) {
            return JsonNumberSchema.builder().description(description).build();
        }
        if ("boolean".equals(type)) {
            return JsonBooleanSchema.builder().description(description).build();
        }
        if ("array".equals(type)) {
            JsonSchemaElement items = schema.get("items") instanceof Map
                    ? toSchemaElement((Map<String, Object>) schema.get("items"))
                    : new JsonStringSchema();
            return JsonArraySchema.builder().description(description).items(items).build();
        }
        if ("object".equals(type)) {
            return toObjectSchema(schema);
        }
        return JsonStringSchema.builder().description(description).build();
    }
}
