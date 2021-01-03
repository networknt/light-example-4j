
package com.networknt.petstore.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.aws.lambda.LightRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * App for requests to Lambda function. This class is generated, so please don't modify it. For developers, you need to
 * put your business logic in the BusinessHandler so that it won't be overwritten with regeneration.
 *
 * @author Steve Hu
 */
public class App extends LightRequestHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        try {
            if(logger.isDebugEnabled()) logger.debug(objectMapper.writeValueAsString(input));
        } catch (Exception e) {
            e.printStackTrace();
        }
        APIGatewayProxyResponseEvent response = interceptRequest(input, context);
        if(response != null) {
            return interceptResponse(response);
        }
        BusinessHandler handler = new BusinessHandler();
        return interceptResponse(handler.handleRequest(input, context));
    }
}
