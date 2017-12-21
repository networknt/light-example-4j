
package com.networknt.tram.todolist.handler;

import com.networknt.config.Config;
import com.networknt.service.SingletonServiceFactory;
import com.networknt.tram.todolist.view.TodoView;
import com.networknt.tram.todolist.view.TodoViewService;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.List;


public class TodoviewsGetHandler implements HttpHandler {

    private TodoViewService todoViewService =  (TodoViewService) SingletonServiceFactory.getBean(TodoViewService.class);

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        String searchValue = exchange.getQueryParameters().get("searchValue").getFirst();

        List<TodoView> todos =  todoViewService.search(searchValue);

        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(todos));
        
    }
}
