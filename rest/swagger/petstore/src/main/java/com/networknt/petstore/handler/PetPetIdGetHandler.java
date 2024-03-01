
package com.networknt.petstore.handler;

import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.Map;

public class PetPetIdGetHandler implements LightHttpHandler {
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

            exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
             exchange.getResponseSender().send(" {\n                \"photoUrls\": [\n                  \"aeiou\"\n                ],\n                \"name\": \"doggie\",\n                \"id\": 123456789,\n                \"category\": {\n                  \"name\": \"aeiou\",\n                  \"id\": 123456789\n                },\n                \"tags\": [\n                  {\n                    \"name\": \"aeiou\",\n                    \"id\": 123456789\n                  }\n                ],\n                \"status\": \"aeiou\"\n              }");

    }
}
