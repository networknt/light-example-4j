package com.networknt.eventuate.todolist.command;

import com.networknt.eventuate.todolist.common.model.TodoInfo;


public class CreateTodoCommand implements TodoCommand {

    private TodoInfo todo;

    public CreateTodoCommand(TodoInfo todo) {
        this.todo = todo;
    }

    public TodoInfo getTodo() {
        return todo;
    }
}
