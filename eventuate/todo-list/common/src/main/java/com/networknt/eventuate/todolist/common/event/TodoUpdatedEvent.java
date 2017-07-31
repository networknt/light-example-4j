package com.networknt.eventuate.todolist.common.event;


import com.networknt.eventuate.todolist.common.model.TodoInfo;

import java.util.Map;

public class TodoUpdatedEvent implements TodoEvent {

    private TodoInfo todo;


    private TodoUpdatedEvent() {
    }

    public TodoUpdatedEvent(TodoInfo todo) {
        this.todo = todo;
    }

    public TodoInfo getTodo() {
        return todo;
    }

    public void setTodo(TodoInfo todo) {
        this.todo = todo;
    }
}