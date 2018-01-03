package com.networknt.tram.todolist.command;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface TodoRepository {

    void setConnection(Connection connection);

    List<Todo>  getAll();

   Todo findOne(String id);

    Todo save(Todo todo) throws SQLException;

    Todo update(Todo todo) throws TodoNotFoundException, SQLException;

    void delete(String id) throws SQLException;
}
