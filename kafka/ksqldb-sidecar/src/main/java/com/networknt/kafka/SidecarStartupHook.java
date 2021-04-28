package com.networknt.kafka;

import com.networknt.server.StartupHookProvider;
import io.confluent.ksql.api.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SidecarStartupHook implements StartupHookProvider {
    private static final Logger logger = LoggerFactory.getLogger(SidecarStartupHook.class);
    //private static final String QUERY = "SELECT * from TEST EMIT CHANGES;";
    private static final String QUERY = "SELECT * from USER_TABLE EMIT CHANGES;";
    private static String KSQLDB_SERVER_HOST = "localhost";
    private static int KSQLDB_SERVER_HOST_PORT = 8088;
    public static Client client = null;

    @Override
    public void onStartup() {
        ClientOptions options = ClientOptions.create()
                .setHost(KSQLDB_SERVER_HOST)
                .setPort(KSQLDB_SERVER_HOST_PORT);
        client = Client.create(options);
        Map<String, Object> properties = new HashMap<>();
        properties.put("auto.offset.reset", "earliest");
        try {
            client.streamQuery(QUERY, properties)
                    .thenAccept(streamedQueryResult -> {
                        System.out.println("Query has started. Query ID: " + streamedQueryResult.queryID());
                        RowSubscriber subscriber = new RowSubscriber();
                        streamedQueryResult.subscribe(subscriber);
                    }).exceptionally(e -> {
                        System.out.println("Request failed: " + e);
                        return null;
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
