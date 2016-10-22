package com.networknt.router;

import com.networknt.db.mysql.MysqlStartupHookProvider;
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
        System.out.println("PathHandlerProvider.getHandler is called ds = " + MysqlStartupHookProvider.ds);
        HttpHandler handler = Handlers.routing()
                .add(Methods.GET, "/text", new TextHandler())
                .add(Methods.GET, "/db/mysql", new DbSqlHandler(MysqlStartupHookProvider.ds, false))
                .add(Methods.GET, "/queries/mysql", new DbSqlHandler(MysqlStartupHookProvider.ds, true))
                .add(Methods.GET, "/updates/mysql", new UpdatesSqlHandler(MysqlStartupHookProvider.ds))
                ;
        return handler;
    }
}
