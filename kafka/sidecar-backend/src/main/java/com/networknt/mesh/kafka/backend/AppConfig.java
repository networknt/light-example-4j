package com.networknt.mesh.kafka.backend;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {
    public static final String CONFIG_NAME = "api-config";

    public Map<String, ServiceRef> apiServiceRef = new HashMap<>();
    public Map<String, String> apiQuery = new HashMap<>();

    public AppConfig() {}

    public Map<String, ServiceRef> getApiServiceRef() {
        return apiServiceRef;
    }

    public void setApiServiceRef(Map<String, ServiceRef> apiServiceRef) {
        this.apiServiceRef = apiServiceRef;
    }

    public Map<String, String> getApiQuery() {
        return apiQuery;
    }

    public void setApiQuery(Map<String, String> apiQuery) {
        this.apiQuery = apiQuery;
    }
}
