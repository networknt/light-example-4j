package com.networknt.mesh.kafka.backend.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.networknt.body.BodyHandler;
import com.networknt.client.http.Http2ServiceRequest;
import com.networknt.client.http.Http2ServiceResponse;
import com.networknt.config.Config;
import com.networknt.config.JsonMapper;
import com.networknt.handler.LightHttpHandler;
import com.networknt.status.Status;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.util.List;
import java.util.Map;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/    private static String KAFKA_SIDECAR_CALL_ERROR = "ERR30001";
*/
public class DeadLetterQueueActivePostHandler extends ExampleBaseHandler implements LightHttpHandler {

    private static String KAFKA_SIDECAR_CALL_ERROR = "ERR30001";

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        List<Map<String, Object>> map = (List)exchange.getAttachment(BodyHandler.REQUEST_BODY);

        try {
            Http2ServiceRequest dlqProcedure = getHttp2ServiceRequest("dqlActiveProcedureAPI");

            System.out.println("request: " + JsonMapper.toJson(map));
            dlqProcedure.setRequestBody(JsonMapper.toJson(map));
            Http2ServiceResponse result = dlqProcedure.call().get();
            String resultStr = result.getClientResponseBody();
            List<Map<String, Object>> response = Config.getInstance().getMapper().readValue(resultStr, new TypeReference<>(){});
            exchange.setStatusCode(200);
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exchange.getResponseSender().send(JsonMapper.toJson(response));
        } catch (Exception e) {
            logger.error("error happen: " + e);
            Status status = new Status(KAFKA_SIDECAR_CALL_ERROR);
            status.setDescription(e.getMessage());
            setExchangeStatus(exchange, status);
        }


    }

}
