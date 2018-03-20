
package com.networknt.multidb.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.config.Config;
import com.networknt.multidb.DbStartupHookProvider;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Db2GetHandler implements HttpHandler {
    private static final DataSource ds = (DataSource) DbStartupHookProvider.dbMap.get("db2");
    private static final ObjectMapper mapper = Config.getInstance().getMapper();

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }
        List<String> result = new ArrayList<>();
        try (final Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT msg FROM t");
             ResultSet resultSet = statement.executeQuery()) {
            while(resultSet.next()) {
                result.add(resultSet.getString("msg"));
            }
        }
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send(mapper.writeValueAsString(result));
    }
}
