package com.networknt.webserver.handler;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by stevehu on 2016-10-27.
 */
public class WebServerConfig {
    String base;
    int transferMinSize;

    @JsonIgnore
    String description;

    public WebServerConfig() {
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public int getTransferMinSize() {
        return transferMinSize;
    }

    public void setTransferMinSize(int transferMinSize) {
        this.transferMinSize = transferMinSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
