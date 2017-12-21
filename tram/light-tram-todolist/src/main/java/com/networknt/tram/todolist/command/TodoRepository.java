package com.networknt.tram.todolist.command;


import java.util.List;
import java.util.Map;

public interface TodoRepository {

    List<Todo>  getAll();

   Todo findOne(String id);

    Todo save(Todo todo);

    Todo update(Todo todo) throws  TodoNotFoundException;

    void delete(String id);
}
