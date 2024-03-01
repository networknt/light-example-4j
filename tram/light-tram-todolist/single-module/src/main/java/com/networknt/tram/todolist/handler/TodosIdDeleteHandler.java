
package com.networknt.tram.todolist.handler;

import com.networknt.service.SingletonServiceFactory;
import com.networknt.tram.todolist.command.TodoCommandService;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;


public class TodosIdDeleteHandler implements HttpHandler {

    private TodoCommandService todoCommandService = (TodoCommandService)SingletonServiceFactory.getBean(TodoCommandService.class);
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {


        String id = exchange.getQueryParameters().get("id").getFirst();
        todoCommandService.delete(id);
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send("delete todo successfully!");

    }
}
