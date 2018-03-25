
package com.networknt.todolist.query.handler;

import com.networknt.config.Config;
import com.networknt.eventuate.todolist.TodoQueryService;
import com.networknt.eventuate.todolist.common.model.TodoInfo;
import com.networknt.service.SingletonServiceFactory;
import com.networknt.utility.NioUtils;
import com.networknt.rpc.Handler;
import com.networknt.rpc.router.ServiceHandler;
import io.undertow.server.HttpServerExchange;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

@ServiceHandler(id="lightapi.net/todo/gettodos/0.1.0")
public class GetAllTodos implements Handler {
    TodoQueryService service =
            (TodoQueryService) SingletonServiceFactory.getBean(TodoQueryService.class);

    @Override
    public ByteBuffer handle(HttpServerExchange exchange, Object input)  {

        List<Map<String, TodoInfo>> resultAll = service.getAll();
        String returnMessage = null;
        try {
            returnMessage = Config.getInstance().getMapper().writeValueAsString(resultAll);
        } catch ( Exception e) {

        }
        return NioUtils.toByteBuffer("{\"message\":" +returnMessage + "}");

    }
}
