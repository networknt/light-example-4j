
package com.networknt.petstore.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * App is the entry point for requests to Lambda function. This class is generated, so please don't modify it.
 * For developers, you need to put business logic in the BusinessHandler so that it won't be overwritten with
 * regeneration.
 *
 * @author Steve Hu
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent request, final Context context) {
        try {
            if(logger.isDebugEnabled()) logger.debug("request = {}", objectMapper.writeValueAsString(request));
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        BusinessHandler handler = new BusinessHandler();
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);
        try {
            if(logger.isDebugEnabled()) logger.debug("response = {}", objectMapper.writeValueAsString(response));
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return response;
    }
}
