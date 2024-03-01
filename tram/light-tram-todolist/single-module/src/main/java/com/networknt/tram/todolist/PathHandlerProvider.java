
package com.networknt.tram.todolist;

import com.networknt.config.Config;
import com.networknt.handler.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import com.networknt.info.ServerInfoGetHandler;
import com.networknt.health.HealthGetHandler;
import com.networknt.tram.todolist.handler.*;

public class PathHandlerProvider implements HandlerProvider {
    @Override
    public HttpHandler getHandler() {
        return Handlers.routing()

            .add(Methods.GET, "/v1/todoviews", new TodoviewsGetHandler())

            .add(Methods.GET, "/v1/health", new HealthGetHandler())

            .add(Methods.POST, "/v1/todos", new TodosPostHandler())

            .add(Methods.GET, "/v1/server/info", new ServerInfoGetHandler())

            .add(Methods.DELETE, "/v1/todos/{id}", new TodosIdDeleteHandler())

            .add(Methods.PUT, "/v1/todos/{id}", new TodosIdPutHandler())

        ;
    }
}
