package com.networknt.db.mysql;

import com.networknt.config.Config;
import com.networknt.server.StartupHookProvider;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * Created by steve on 22/10/16.
 */
public class MysqlStartupHookProvider implements StartupHookProvider {

    static String CONFIG_NAME = "mysql";
    public static HikariDataSource ds;

    public void onStartup() {
        System.out.println("MysqlStartupHookProvider is called");
        initDataSource();
        System.out.println("MysqlStartupHookProvider ds = " + ds);

    }

    static void initDataSource() {
        MysqlConfig config = (MysqlConfig) Config.getInstance().getJsonObjectConfig(CONFIG_NAME, MysqlConfig.class);
        ds = new HikariDataSource();
        ds.setJdbcUrl(config.getJdbcUrl());
        ds.setUsername(config.getUsername());
        ds.setPassword(config.getPassword());
        ds.setMaximumPoolSize(config.getMaximumPoolSize());
        ds.addDataSourceProperty("useServerPrepStmts", config.isUseServerPrepStmts());
        ds.addDataSourceProperty("CachePrepStmts", config.isCachePrepStmts());
        ds.addDataSourceProperty("CacheCallableStmts", config.isCacheCallableStmts());
        ds.addDataSourceProperty("PrepStmtCacheSize", config.getPrepStmtCacheSize());
        ds.addDataSourceProperty("PrepStmtCacheSqlLimit", config.getPrepStmtCacheSqlLimit());
    }
}
