package com.networknt.llmchat.mcp;

import com.networknt.config.Config;

public class McpClientConfig {
    public static final String CONFIG_NAME = "mcp-client";
    private static final McpClientConfig CONFIG = (McpClientConfig) Config.getInstance()
            .getJsonObjectConfig(CONFIG_NAME, McpClientConfig.class);

    private boolean enabled;
    private String gatewayUrl;
    private String path;
    private int timeout;
    private boolean verifyHostname;

    public static McpClientConfig load() {
        return CONFIG;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isVerifyHostname() {
        return verifyHostname;
    }

    public void setVerifyHostname(boolean verifyHostname) {
        this.verifyHostname = verifyHostname;
    }
}
