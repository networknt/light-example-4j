package com.networknt.mesh.kafka.backend;

public class ApplicationConfig {
    public static String CONFIG_NAME = "application";
    private int keySchemaVersion;
    private int valueSchemaVersion;
    private String targetTopicName;
    private String sidecarProducerUrl;

    public ApplicationConfig() {
    }

    public int getKeySchemaVersion() {
        return keySchemaVersion;
    }

    public void setKeySchemaVersion(int keySchemaVersion) {
        this.keySchemaVersion = keySchemaVersion;
    }

    public int getValueSchemaVersion() {
        return valueSchemaVersion;
    }

    public void setValueSchemaVersion(int valueSchemaVersion) {
        this.valueSchemaVersion = valueSchemaVersion;
    }

    public String getTargetTopicName() {
        return targetTopicName;
    }

    public void setTargetTopicName(String targetTopicName) {
        this.targetTopicName = targetTopicName;
    }

    public String getSidecarProducerUrl() {
        return sidecarProducerUrl;
    }

    public void setSidecarProducerUrl(String sidecarProducerUrl) {
        this.sidecarProducerUrl = sidecarProducerUrl;
    }
}
