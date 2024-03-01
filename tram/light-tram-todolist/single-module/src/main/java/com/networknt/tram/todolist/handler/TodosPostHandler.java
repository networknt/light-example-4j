
package com.networknt.tram.todolist.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.body.BodyHandler;
import com.networknt.config.Config;
import com.networknt.service.SingletonServiceFactory;
import com.networknt.tram.todolist.command.CreateTodoRequest;
import com.networknt.tram.todolist.command.Todo;
import com.networknt.tram.todolist.command.TodoCommandService;
import com.networknt.tram.todolist.command.TodoRepository;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.Map;

public class TodosPostHandler implements HttpHandler {

    private TodoCommandService todoCommandService = (TodoCommandService)SingletonServiceFactory.getBean(TodoCommandService.class);

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        // add a new object
        Map s = (Map)exchange.getAttachment(BodyHandler.REQUEST_BODY);
        String json = mapper.writeValueAsString(s);
        CreateTodoRequest createTodoRequest = mapper.readValue(json, CreateTodoRequest.class);
        Todo todo = todoCommandService.create(createTodoRequest);

        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(todo));

    }
}
