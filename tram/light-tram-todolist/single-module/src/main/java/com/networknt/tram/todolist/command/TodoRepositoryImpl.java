package com.networknt.tram.todolist.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TodoRepositoryImpl implements TodoRepository {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private DataSource dataSource;

    public TodoRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDataSource(DataSource dataSource) {this.dataSource = dataSource;}
    @Override
    public List<Todo> getAll() {
        List<Todo> todos = new ArrayList<>();

        try (final Connection connection = dataSource.getConnection()){
            String psSelect = "SELECT ID, TITLE, COMPLETED, ORDER_ID FROM todo_db.TODO WHERE ACTIVE_FLG = 'Y' order by ORDER_ID asc";
            PreparedStatement stmt = connection.prepareStatement(psSelect);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Todo todo = new Todo();
                todo.setTitle(rs.getString("ID"));
                todo.setTitle(rs.getString("TITLE"));
                todo.setCompleted(rs.getBoolean("COMPLETED"));
                todo.setExecutionOrder(rs.getInt("ORDER_ID"));
                todos.add(todo);
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

        return todos;
    }

    @Override
    public  Todo findOne(String id) {
        Todo todo = null;
        try (final Connection connection = dataSource.getConnection()){
            String psSelect = "SELECT ID, TITLE, COMPLETED, ORDER_ID FROM todo_db.TODO WHERE ACTIVE_FLG = 'Y' AND ID = ?";
            PreparedStatement stmt = connection.prepareStatement(psSelect);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs == null || rs.getFetchSize() > 1) {
                logger.error("incorrect fetch result {}", id);
            }
            while (rs.next()) {
                todo = new Todo();
                todo.setId(rs.getString("ID"));
                todo.setTitle(rs.getString("TITLE"));
                todo.setCompleted(rs.getBoolean("COMPLETED"));
                todo.setExecutionOrder(rs.getInt("ORDER_ID"));
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

        return todo;
    }

    @Override
    public Todo save(Todo todo) {
        try (final Connection connection = dataSource.getConnection()){
            String psInsert = "INSERT INTO todo_db.TODO (ID, TITLE, COMPLETED, ORDER_ID) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(psInsert);
            stmt.setString(1, todo.getId());
            stmt.setString(2, todo.getTitle());
            stmt.setBoolean(3, todo.isCompleted());
            stmt.setInt(4, todo.getExecutionOrder());
            int count = stmt.executeUpdate();
            if (count != 1) {
                logger.error("Failed to insert TODO: {}", todo.getId());
            } else {
                return todo;
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

        return null;
    }

    @Override
    public Todo update(Todo todo) {
        try (final Connection connection = dataSource.getConnection()){
            String psUpdate = "UPDATE todo_db.TODO SET TITLE=?, COMPLETED=?, ORDER_ID=? WHERE ID=? ";
            PreparedStatement stmt = connection.prepareStatement(psUpdate);

            stmt.setString(1, todo.getTitle());
            stmt.setBoolean(2, todo.isCompleted());
            stmt.setInt(3, todo.getExecutionOrder());
            stmt.setString(4, todo.getId());
            int count = stmt.executeUpdate();
            if (count != 1) {
                logger.error("Failed to update TODO: {}", todo.getId());
            } else {
                return todo;
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

        return null;
    }

    @Override
    public void delete(String id) {
        try (final Connection connection = dataSource.getConnection()){
            String psDelete = "UPDATE todo_db.TODO SET ACTIVE_FLG = 'N' WHERE ID = ?";
            PreparedStatement psEntity = connection.prepareStatement(psDelete);
            psEntity.setString(1, id);
            int count = psEntity.executeUpdate();
            if (count != 1) {
                logger.error("Failed to update TODO: {}", id);
            }

        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

    }


}
