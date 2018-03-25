
package com.networknt.todolist.query.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.config.Config;
import com.networknt.eventuate.todolist.TodoQueryService;
import com.networknt.eventuate.todolist.common.model.TodoInfo;
import com.networknt.service.SingletonServiceFactory;
import com.networknt.utility.NioUtils;
import com.networknt.rpc.Handler;
import com.networknt.rpc.router.ServiceHandler;
import io.undertow.server.HttpServerExchange;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@ServiceHandler(id="lightapi.net/todo/gettodo/0.1.0")
public class GetTodoById implements Handler {
    TodoQueryService service =
            (TodoQueryService) SingletonServiceFactory.getBean(TodoQueryService.class);

    @Override
    public ByteBuffer handle(HttpServerExchange exchange, Object input) {

        JsonNode inputPara = Config.getInstance().getMapper().valueToTree(input);

        String id = inputPara.findPath("id").asText();


        CompletableFuture<Map<String, TodoInfo>> result = service.findById(id);
        String returnMessage = null;
        try {
            returnMessage = Config.getInstance().getMapper().writeValueAsString(result);
        } catch (Exception e) {

        }
        return NioUtils.toByteBuffer("{\"message\":" + returnMessage + "}");
    }
}
