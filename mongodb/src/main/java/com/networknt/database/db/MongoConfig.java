package com.networknt.database.db;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by stevehu on 2017-03-09.
 */
public class MongoConfig {
    @JsonIgnore
    String description;
    String host;
    String name;

    public MongoConfig() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
