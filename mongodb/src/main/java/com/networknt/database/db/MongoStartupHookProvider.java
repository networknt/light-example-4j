package com.networknt.database.db;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.networknt.config.Config;
import com.networknt.server.StartupHookProvider;

/**
 * Created by stevehu on 2017-03-09.
 */
public class MongoStartupHookProvider implements StartupHookProvider {

    static String CONFIG_NAME = "mongo";
    public static MongoDatabase db;

    public void onStartup() {
        System.out.println("MongoStartupHookProvider is called");
        initDataSource();
        System.out.println("MongoStartupHookProvider db = " + db);

    }

    static void initDataSource() {
        MongoConfig config = (MongoConfig) Config.getInstance().getJsonObjectConfig(CONFIG_NAME, MongoConfig.class);
        MongoClientOptions.Builder clientOptions = new MongoClientOptions.Builder();
        clientOptions.minConnectionsPerHost(10);//minPoolSize
        clientOptions.connectionsPerHost(50);//maxPoolSize
        MongoClient mongoClient = new MongoClient(new ServerAddress(config.getHost()), clientOptions.build());
        db = mongoClient.getDatabase(config.getName());
    }
}
