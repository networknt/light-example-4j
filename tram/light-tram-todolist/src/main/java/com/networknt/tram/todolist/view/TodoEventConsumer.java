package com.networknt.tram.todolist.view;

import com.networknt.tram.event.subscriber.DomainEventHandlers;
import com.networknt.tram.event.subscriber.DomainEventHandlersBuilder;
import com.networknt.tram.todolist.command.Todo;
import com.networknt.tram.todolist.common.TodoCreated;
import com.networknt.tram.todolist.common.TodoDeleted;
import com.networknt.tram.todolist.common.TodoUpdated;


public class TodoEventConsumer {

  private TodoViewServiceImpl todoViewService;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType(Todo.class.getName())
            .onEvent(TodoCreated.class, dee -> {
              TodoCreated todoCreated = dee.getEvent();
              todoViewService.index(new TodoView(dee.getAggregateId(),
                  todoCreated.getTitle(), todoCreated.isCompleted(), todoCreated.getExecutionOrder()));
            })
            .onEvent(TodoUpdated.class, dee -> {
              TodoUpdated todoUpdated = dee.getEvent();
              todoViewService.index(new TodoView(dee.getAggregateId(),
                  todoUpdated.getTitle(), todoUpdated.isCompleted(), todoUpdated.getExecutionOrder()));
            })
            .onEvent(TodoDeleted.class, dee ->
                    todoViewService.remove(dee.getAggregateId()))
            .build();
  }
}
