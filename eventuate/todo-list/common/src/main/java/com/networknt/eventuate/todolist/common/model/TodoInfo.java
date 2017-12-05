package com.networknt.eventuate.todolist.common.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

public class TodoInfo {
    @JsonIgnore
    private String id;
    private String title;
    private boolean completed;
    private int order;

    public TodoInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
