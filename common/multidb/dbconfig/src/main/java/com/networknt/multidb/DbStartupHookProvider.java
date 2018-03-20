package com.networknt.multidb;

import com.networknt.config.Config;
import com.networknt.server.StartupHookProvider;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DbStartupHookProvider implements StartupHookProvider {
    public static final String DATA_SOURCE = "datasource";
    public static Map<String, DataSource> dbMap = new HashMap<>();

    @Override
    public void onStartup() {
        Map<String, Object> dataSourceMap = (Map<String, Object>) Config.getInstance().getJsonMapConfig(DATA_SOURCE);
        // iterate all db config
        dataSourceMap.forEach((k, v) -> {
            HikariDataSource ds = new HikariDataSource();
            ds.setJdbcUrl(((Map<String, String>)v).get("jdbcUrl"));
            ds.setUsername(((Map<String, String>)v).get("username"));
            ds.setPassword(((Map<String, String>)v).get("password"));
            Map<String, String> configParams = (Map<String, String>)((Map<String, Object>)v).get("parameters");
            configParams.forEach((p, q) -> ds.addDataSourceProperty(p, q));
            dbMap.put(k, ds);
        });
    }
}
