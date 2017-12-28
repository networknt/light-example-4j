package com.networknt.tram.todolist.command;


import com.networknt.eventuate.jdbc.IdGenerator;
import com.networknt.eventuate.jdbc.IdGeneratorImpl;

public class Todo {

  private static IdGenerator idGenerator = new IdGeneratorImpl();
  private String id;

  private String title;

  private boolean completed;

  private int executionOrder;

  public Todo() {
  }

  public Todo(String title, boolean completed, int executionOrder) {
    this.id = idGenerator.genId().asString();
    this.title = title;
    this.completed = completed;
    this.executionOrder = executionOrder;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
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

  public int getExecutionOrder() {
    return executionOrder;
  }

  public void setExecutionOrder(int executionOrder) {
    this.executionOrder = executionOrder;
  }
}
