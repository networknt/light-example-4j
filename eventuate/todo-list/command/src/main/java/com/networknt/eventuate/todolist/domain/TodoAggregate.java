package com.networknt.eventuate.todolist.domain;


import com.networknt.eventuate.common.Event;
import com.networknt.eventuate.common.EventUtil;
import com.networknt.eventuate.common.ReflectiveMutableCommandProcessingAggregate;
import com.networknt.eventuate.todolist.command.CreateTodoCommand;
import com.networknt.eventuate.todolist.command.DeleteTodoCommand;
import com.networknt.eventuate.todolist.command.TodoCommand;
import com.networknt.eventuate.todolist.command.UpdateTodoCommand;
import com.networknt.eventuate.todolist.common.event.TodoCreatedEvent;
import com.networknt.eventuate.todolist.common.event.TodoDeletedEvent;
import com.networknt.eventuate.todolist.common.event.TodoUpdatedEvent;
import com.networknt.eventuate.todolist.common.model.TodoInfo;

import java.util.Collections;
import java.util.List;


public class TodoAggregate extends ReflectiveMutableCommandProcessingAggregate<TodoAggregate, TodoCommand> {

    private TodoInfo todo;

    private boolean deleted;

    public List<Event> process(CreateTodoCommand cmd) {
        if (this.deleted) {
            return Collections.emptyList();
        }
        return EventUtil.events(new TodoCreatedEvent(cmd.getTodo()));
    }

    public List<Event> process(UpdateTodoCommand cmd) {
        if (this.deleted) {
            return Collections.emptyList();
        }
        return EventUtil.events(new TodoUpdatedEvent(cmd.getTodo()));
    }

    public List<Event> process(DeleteTodoCommand cmd) {
        if (this.deleted) {
            return Collections.emptyList();
        }
        return EventUtil.events(new TodoDeletedEvent());
    }


    public void apply(TodoCreatedEvent event) {
        this.todo = event.getTodo();
    }

    public void apply(TodoUpdatedEvent event) {
        this.todo = event.getTodo();
    }

    public void apply(TodoDeletedEvent event) {
        this.deleted = true;
    }

    public TodoInfo getTodo() {
        return todo;
    }

}


