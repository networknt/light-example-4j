
package com.networknt.petstore.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.networknt.aws.lambda.LightLambdaInterceptor;

/**
 * App for requests to Lambda function. This class is generated, so please don't modify it. For developers, you need to
 * put your business logic in the BusinessHandler so that it won't be overwritten with regeneration.
 *
 * @author Steve Hu
 */
public class App extends LightLambdaInterceptor implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, final Context context) {
        input = interceptRequest(input, context);
        BusinessHandler handler = new BusinessHandler();
        return interceptResponse(handler.handleRequest(input, context));
    }
}
