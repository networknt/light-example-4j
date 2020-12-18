
package com.networknt.petstore.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.networknt.aws.lambda.LightRequestHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler for requests to Lambda function.
 */
public class App extends LightRequestHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        /* Generated Begin */
        APIGatewayProxyResponseEvent response = interceptRequest(input, context);
        if(response != null) {
            return interceptResponse(response);
        }
        /* Generated End */

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        response = new APIGatewayProxyResponseEvent().withHeaders(headers);
        String output = "";
        output = "{\"id\":1,\"name\":\"Jessica Right\",\"tag\":\"pet\"}";
        response.withStatusCode(200)
                .withBody(output);

        /* Generated Begin */
        return interceptResponse(response);
        /* Generated End */
    }
}
