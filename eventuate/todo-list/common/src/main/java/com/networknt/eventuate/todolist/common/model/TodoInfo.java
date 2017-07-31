package com.networknt.eventuate.todolist.common.model;


public class TodoInfo {
    private String title;
    private boolean completed;
    private int orderId;

    public TodoInfo() {
    }

    public TodoInfo(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getOrder() {
        return orderId;
    }

    public void setOrder(int orderId) {
        this.orderId = orderId;
    }
}
