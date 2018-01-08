
package com.networknt.tram.todolist.command.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.body.BodyHandler;
import com.networknt.config.Config;
import com.networknt.service.SingletonServiceFactory;
import com.networknt.tram.todolist.command.Todo;
import com.networknt.tram.todolist.command.TodoCommandService;
import com.networknt.tram.todolist.command.TodoNotFoundException;
import com.networknt.tram.todolist.command.UpdateTodoRequest;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.Map;

public class TodosIdPutHandler implements HttpHandler {
    private TodoCommandService todoCommandService = (TodoCommandService) SingletonServiceFactory.getBean(TodoCommandService.class);
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        String id = exchange.getQueryParameters().get("id").getFirst();

        ObjectMapper mapper = new ObjectMapper();

        // add a new object
        Map s = (Map)exchange.getAttachment(BodyHandler.REQUEST_BODY);
        String json = mapper.writeValueAsString(s);
        UpdateTodoRequest updateTodoRequest = mapper.readValue(json, UpdateTodoRequest.class);

        try {
            Todo todo = todoCommandService.update(id, updateTodoRequest);
            exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
            exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(todo));
        } catch ( TodoNotFoundException e) {
            exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
            exchange.getResponseSender().send("No record been updated");
        }
    }
}
