package com.networknt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.config.Config;
import com.networknt.db.mysql.MysqlStartupHookProvider;
import com.networknt.model.World;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * Created by steve on 22/10/16.
 */
public class DbSqlHandler implements HttpHandler {
    private final DataSource ds;
    private final boolean multiple;
    private final ObjectMapper mapper;

    public DbSqlHandler(DataSource ds, boolean multiple) {
        this.ds = ds;
        this.multiple = multiple;
        this.mapper = Config.getInstance().getMapper();
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }

        int queries = 1;
        if(multiple)
        {
            queries = Helper.getQueries(exchange);
        }

        World[] worlds = new World[queries];
        try (final Connection connection = ds.getConnection()) {
            Map<Integer, Future<World>> futureWorlds = new ConcurrentHashMap<>();
            for (int i = 0; i < queries; i++) {
                futureWorlds.put(i, Helper.EXECUTOR.submit(new Callable<World>(){
                    @Override
                    public World call() throws Exception {
                        try (PreparedStatement statement = connection.prepareStatement(
                                "SELECT * FROM world WHERE id = ?",
                                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {

                            statement.setInt(1, Helper.randomWorld());
                            ResultSet resultSet = statement.executeQuery();
                            resultSet.next();
                            return new World(
                                    resultSet.getInt("id"),
                                    resultSet.getInt("randomNumber"));
                        }
                    }
                }));
            }

            for (int i = 0; i < queries; i++) {
                worlds[i] = futureWorlds.get(i).get();
            }
        }
        exchange.getResponseHeaders().put(
                Headers.CONTENT_TYPE, "application/json");

        if (multiple)
        {
            // If a multiple query then response must be an array
            exchange.getResponseSender().send(mapper.writeValueAsString(worlds));
        }
        else
        {
            // If a single query then response must be an object
            exchange.getResponseSender().send(mapper.writeValueAsString(worlds[0]));
        }
    }
}
