package com.networknt.db.postgres;

import com.networknt.config.Config;
import com.networknt.db.mysql.MysqlConfig;
import com.networknt.server.StartupHookProvider;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Created by steve on 22/10/16.
 */
public class PostgresStartupHookProvider implements StartupHookProvider {

    static String CONFIG_NAME = "postgres";
    public static HikariDataSource ds;

    public void onStartup() {
        initDataSource();

    }

    static void initDataSource() {
        PostgresConfig config = (PostgresConfig) Config.getInstance().getJsonObjectConfig(CONFIG_NAME, PostgresConfig.class);
        ds = new HikariDataSource();
        ds.setJdbcUrl(config.getJdbcUrl());
        ds.setUsername(config.getUsername());
        ds.setPassword(config.getPassword());
        ds.setMaximumPoolSize(config.getMaximumPoolSize());
    }
}
