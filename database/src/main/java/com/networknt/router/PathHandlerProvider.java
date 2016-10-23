package com.networknt.router;

import com.networknt.db.mysql.MysqlStartupHookProvider;
import com.networknt.db.postgres.PostgresStartupHookProvider;
import com.networknt.handler.DbSqlHandler;
import com.networknt.handler.TextHandler;
import com.networknt.handler.UpdatesSqlHandler;
import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.util.Methods;

/**
 * Created by steve on 22/10/16.
 */
public class PathHandlerProvider implements HandlerProvider {

    public HttpHandler getHandler() {
        HttpHandler handler = Handlers.routing()
                .add(Methods.GET, "/text", new TextHandler())
                .add(Methods.GET, "/db/mysql", new DbSqlHandler(MysqlStartupHookProvider.ds, false))
                .add(Methods.GET, "/queries/mysql", new DbSqlHandler(MysqlStartupHookProvider.ds, true))
                .add(Methods.GET, "/updates/mysql", new UpdatesSqlHandler(MysqlStartupHookProvider.ds))
                .add(Methods.GET, "/db/postgres", new DbSqlHandler(PostgresStartupHookProvider.ds, false))
                .add(Methods.GET, "/queries/postgres", new DbSqlHandler(PostgresStartupHookProvider.ds, true))
                .add(Methods.GET, "/updates/postgres", new UpdatesSqlHandler(PostgresStartupHookProvider.ds))
                ;
        return handler;
    }
}
