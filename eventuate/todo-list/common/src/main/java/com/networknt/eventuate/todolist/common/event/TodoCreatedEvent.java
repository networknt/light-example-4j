package com.networknt.eventuate.todolist.common.event;

import com.networknt.eventuate.todolist.common.model.TodoInfo;


public class TodoCreatedEvent implements TodoEvent {

    private TodoInfo todo;

    private TodoCreatedEvent() {
    }

    public TodoCreatedEvent(TodoInfo todo) {
        this.todo = todo;
    }

    public TodoInfo getTodo() {
        return todo;
    }

    public void setTodo(TodoInfo todo) {
        this.todo = todo;
    }
}