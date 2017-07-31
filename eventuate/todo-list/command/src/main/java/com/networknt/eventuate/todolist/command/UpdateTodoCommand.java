package com.networknt.eventuate.todolist.command;

import com.networknt.eventuate.todolist.common.model.TodoInfo;


public class UpdateTodoCommand implements TodoCommand {
    private TodoInfo todo;

    public UpdateTodoCommand(String id, TodoInfo todo) {
        this.todo = todo;
    }

    public TodoInfo getTodo() {
        return todo;
    }
}
