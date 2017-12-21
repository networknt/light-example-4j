package com.networknt.tram.todolist.command;

import com.networknt.tram.event.common.DomainEvent;
import com.networknt.tram.event.publisher.DomainEventPublisher;
import com.networknt.tram.todolist.common.TodoCreated;
import com.networknt.tram.todolist.common.TodoDeleted;
import com.networknt.tram.todolist.common.TodoUpdated;

import static java.util.Arrays.asList;


public class TodoCommandService {


  private TodoRepository todoRepository;

  private DomainEventPublisher domainEventPublisher;

  public TodoCommandService( TodoRepository todoRepository, DomainEventPublisher domainEventPublisher) {
    this.todoRepository= todoRepository;
    this.domainEventPublisher = domainEventPublisher;
  }

  public Todo create(CreateTodoRequest createTodoRequest) {
    Todo todo = new Todo(createTodoRequest.getTitle(), createTodoRequest.isCompleted(), createTodoRequest.getOrder());
    todo = todoRepository.save(todo);

    publishTodoEvent(todo, new TodoCreated(todo.getTitle(), todo.isCompleted(), todo.getExecutionOrder()));

    return todo;
  }

  private void publishTodoEvent(Todo todo, DomainEvent... domainEvents) {
    domainEventPublisher.publish(Todo.class, todo.getId(), asList(domainEvents));
  }

  private void publishTodoEvent(String id, DomainEvent... domainEvents) {
    domainEventPublisher.publish(Todo.class, id, asList(domainEvents));
  }

  public Todo update(String id, UpdateTodoRequest updateTodoRequest) throws  TodoNotFoundException{
    Todo todo = todoRepository.findOne(id);

    if (todo == null) {
      throw new TodoNotFoundException(id);
    }

    todo.setTitle(updateTodoRequest.getTitle());
    todo.setCompleted(updateTodoRequest.isCompleted());
    todo.setExecutionOrder(updateTodoRequest.getOrder());
    todoRepository.update(todo);

    publishTodoEvent(todo, new TodoUpdated(todo.getTitle(), todo.isCompleted(), todo.getExecutionOrder()));

    return todo;
  }

  public void delete(String id) {
    todoRepository.delete(id);
    publishTodoEvent(id, new TodoDeleted());
  }
}
