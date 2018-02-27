package com.networknt.webserver.handler;

/**
 * @author Steve Hu
 */
public class WebServerConfig {
    String base;
    int transferMinSize;

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
}
