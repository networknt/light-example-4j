package com.networknt.eventuate.todolist.common.event;





public class TodoDeletionRequestedEvent implements TodoBulkEvent {

    private String todoId;

    public TodoDeletionRequestedEvent(String todoId) {
        this.todoId = todoId;
    }

    public TodoDeletionRequestedEvent() {

    }

    public String getTodoId() {
        return todoId;
    }
}
