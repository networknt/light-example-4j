package com.networknt.market.service;

import java.util.HashMap;
import java.util.Map;

public class ServiceMapper {
    public static final String CONFIG_NAME = "service-define";

    private Map<String, ServiceObject> serviceMap = new HashMap<>();


    public ServiceMapper() {
    }

    public Map<String, ServiceObject> getServiceMap() {
        return serviceMap;
    }

    public void setServiceMap(Map<String, ServiceObject> serviceMap) {
        this.serviceMap = serviceMap;
    }
}
