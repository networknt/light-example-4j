package com.networknt.mesh.kafka.backend.handler;


import com.networknt.client.http.Http2ServiceRequest;
import com.networknt.config.Config;
import com.networknt.mesh.kafka.backend.AppConfig;
import com.networknt.mesh.kafka.backend.ServiceRef;
import com.networknt.utility.Constants;
import io.undertow.util.HttpString;

import java.net.URI;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class ExampleBaseHandler {

    AppConfig apiConfig = (AppConfig) Config.getInstance().getJsonObjectConfig(AppConfig.CONFIG_NAME, AppConfig.class);
    static final String JSON_MEDIA_TYPE = "application/json";

    protected Http2ServiceRequest getHttp2ServiceRequest(String serviceName) throws Exception {
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
}
