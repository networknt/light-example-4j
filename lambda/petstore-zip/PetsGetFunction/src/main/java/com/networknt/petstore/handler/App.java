
package com.networknt.petstore.handler;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.aws.lambda.LightRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler for requests to Lambda function.
 */
public class App extends LightRequestHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        /* Generated Begin */
        try {
            if(logger.isDebugEnabled()) logger.debug(objectMapper.writeValueAsString(input));
        } catch (Exception e) {
            e.printStackTrace();
        }
        APIGatewayProxyResponseEvent response = interceptRequest(input, context);
        if(response != null) {
            return interceptResponse(response);
        }
        /* Generated End */

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        response = new APIGatewayProxyResponseEvent().withHeaders(headers);
        String output = "";
        output = "[{\"id\":1,\"name\":\"catten\",\"tag\":\"cat\"},{\"id\":2,\"name\":\"doggy\",\"tag\":\"dog\"}]";
        response.withStatusCode(200)
                .withBody(output);

        /* Generated Begin */
        return interceptResponse(response);
        /* Generated End */
    }
}
