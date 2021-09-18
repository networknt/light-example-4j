package com.networknt.mesh.kafka.backend.handler;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.client.http.Http2ServiceRequest;
import com.networknt.client.http.Http2ServiceResponse;
import com.networknt.config.Config;
import com.networknt.config.JsonMapper;
import com.networknt.handler.LightHttpHandler;
import com.networknt.kafka.entity.KsqlDbPullQueryRequest;
import com.networknt.mesh.kafka.backend.AppConfig;
import com.networknt.mesh.kafka.backend.ServiceRef;
import com.networknt.utility.Constants;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class KsqlDBRowsGetHandler implements LightHttpHandler {

    AppConfig apiConfig = (AppConfig) Config.getInstance().getJsonObjectConfig(AppConfig.CONFIG_NAME, AppConfig.class);
    static final String JSON_MEDIA_TYPE = "application/json";

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String queryname = exchange.getQueryParameters().get("queryname").getFirst();

        KsqlDbPullQueryRequest ksqlDbPullQueryRequest = new KsqlDbPullQueryRequest();
        String query = getQuery(queryname);
        if (exchange.getQueryParameters().get("id") == null)  {
            ksqlDbPullQueryRequest.setTableScanEnable(true);
        } else {
            String id = exchange.getQueryParameters().get("id").getFirst();
            query = query + " where id= \'" + id + "\'";
        }
//        String id = "00OOCDIBCOOCDPBCGP";
//        String query1="select * from QUERYABLE_ECLAIM_SF_PARENTTOPIC where `Id`='"+id+"';";
        ksqlDbPullQueryRequest.setQuery(query + ";");
        ksqlDbPullQueryRequest.setDeserializationError(false);
        Http2ServiceRequest getQueryData = getHttp2ServiceRequest("ksqlActiveAPI");

        System.out.println("request: " + JsonMapper.toJson(ksqlDbPullQueryRequest));
        getQueryData.setRequestBody(JsonMapper.toJson(ksqlDbPullQueryRequest));
        Http2ServiceResponse result = getQueryData.call().get();
        String resultStr = result.getClientResponseBody();
        List<Map<String, Object>> response = mapper.readValue(resultStr, new TypeReference<>(){});
        exchange.setStatusCode(200);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send(JsonMapper.toJson(response));
    }


    private Http2ServiceRequest getHttp2ServiceRequest(String serviceName) throws Exception {
        ServiceRef serviceRef = getService(serviceName);
        if (serviceRef==null) throw new Exception("There is no service config in the api-config.yml file.");

        Http2ServiceRequest http2ServiceRequest =   new Http2ServiceRequest(new URI(serviceRef.getServiceUrl()), serviceRef.getPath(), new HttpString(serviceRef.getMethod()));
        http2ServiceRequest.addRequestHeader("Host", "localhost");
        http2ServiceRequest.addRequestHeader("Transfer-Encoding", "chunked");
        http2ServiceRequest.addRequestHeader("Content-Type", JSON_MEDIA_TYPE);
        http2ServiceRequest.addRequestHeader(Constants.TRACEABILITY_ID_STRING, "NRoVaiqDOsadcMwzrUEknToEZlWLs");
        return http2ServiceRequest;
    }

    protected ServiceRef getService(String name) {
        return apiConfig.getApiServiceRef().get(name);
    }

    protected String getQuery(String name) {
        return apiConfig.getApiQuery().get(name);
    }
}
