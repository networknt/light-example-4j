package com.networknt.database.db;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.networknt.config.Config;
import com.networknt.server.StartupHookProvider;

/**
 * Created by stevehu on 2017-03-09.
 */
public class MongoStartupHookProvider implements StartupHookProvider {

    static String CONFIG_NAME = "mongo";
    public static DB db;

    public void onStartup() {
        System.out.println("MongoStartupHookProvider is called");
        initDataSource();
        System.out.println("MongoStartupHookProvider db = " + db);

    }

    static void initDataSource() {

        MongoConfig config = (MongoConfig) Config.getInstance().getJsonObjectConfig(CONFIG_NAME, MongoConfig.class);
        db = new MongoClient(config.getHost())
                .getDB(config.getName());

    }
}
