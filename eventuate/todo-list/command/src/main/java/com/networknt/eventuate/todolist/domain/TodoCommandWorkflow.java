package com.networknt.eventuate.todolist.domain;

import com.networknt.eventuate.common.EntityWithIdAndVersion;
import com.networknt.eventuate.common.EventHandlerContext;
import com.networknt.eventuate.common.EventHandlerMethod;
import com.networknt.eventuate.common.EventSubscriber;
import com.networknt.eventuate.todolist.command.DeleteTodoCommand;
import com.networknt.eventuate.todolist.common.event.TodoDeletionRequestedEvent;

import java.util.concurrent.CompletableFuture;


@EventSubscriber(id = "todoCommandSideEventHandlers")
public class TodoCommandWorkflow {

    @EventHandlerMethod
    public CompletableFuture<EntityWithIdAndVersion<TodoAggregate>> deleteTodo(EventHandlerContext<TodoDeletionRequestedEvent> ctx) {
        String todoId = ctx.getEvent().getTodoId();

        return ctx.update(TodoAggregate.class, todoId, new DeleteTodoCommand());
    }

}
