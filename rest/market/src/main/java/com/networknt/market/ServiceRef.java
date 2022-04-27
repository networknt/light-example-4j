package com.networknt.market;

import java.util.Objects;

public class ServiceRef {
    String serviceUrl;
    String protocol;
    String serviceId;
    String method;
    String path;
    String environment;
    String requestKey;

    public ServiceRef() {
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceRef that = (ServiceRef) o;
        return Objects.equals(serviceUrl, that.serviceUrl) && Objects.equals(protocol, that.protocol) && Objects.equals(serviceId, that.serviceId) && Objects.equals(method, that.method) && Objects.equals(path, that.path) && Objects.equals(environment, that.environment) && Objects.equals(requestKey, that.requestKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceUrl, protocol, serviceId, method, path, environment, requestKey);
    }
}
