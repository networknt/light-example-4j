package com.networknt.market;

import java.util.HashMap;
import java.util.Map;

public class MarketConfig {
    public static final String CONFIG_NAME = "market";
    private Map<String, ServiceRef> apiServiceRef = new HashMap<>();
    private String proxyUrl;

    public MarketConfig() {
    }

    public Map<String, ServiceRef> getApiServiceRef() {
        return apiServiceRef;
    }

    public void setApiServiceRef(Map<String, ServiceRef> apiServiceRef) {
        this.apiServiceRef = apiServiceRef;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }
}
