package com.networknt.eventuate.todolist;

import com.networknt.eventuate.todolist.common.model.TodoInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoQueryRepositoryImpl implements TodoQueryRepository {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private DataSource dataSource;

    public TodoQueryRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDataSource(DataSource dataSource) {this.dataSource = dataSource;}
    @Override
    public List<Map<String, TodoInfo>> getAll() {
        List<Map<String, TodoInfo>> todos = new ArrayList<Map<String, TodoInfo>>();

        try (final Connection connection = dataSource.getConnection()){
            String psSelect = "SELECT ID, TITLE, COMPLETED, ORDER_ID FROM TODO WHERE ACTIVE_FLG = 'Y' order by ORDER_ID asc";
            PreparedStatement stmt = connection.prepareStatement(psSelect);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TodoInfo todo = new TodoInfo();
                todo.setTitle(rs.getString("TITLE"));
                todo.setCompleted(rs.getBoolean("COMPLETED"));
                todo.setOrder(rs.getInt("ORDER_ID"));
                Map<String, TodoInfo> todoMap = new HashMap<String, TodoInfo>();
                todoMap.put(rs.getString("id"), todo);
                todos.add(todoMap);
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

        return todos;
    }

    @Override
    public Map<String, TodoInfo> findById(String id) {
        Map<String, TodoInfo> todoMap = new HashMap<String, TodoInfo>();
        try (final Connection connection = dataSource.getConnection()){
            String psSelect = "SELECT ID, TITLE, COMPLETED, ORDER_ID FROM TODO WHERE ACTIVE_FLG = 'Y' AND ID = ?";
            PreparedStatement stmt = connection.prepareStatement(psSelect);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs == null || rs.getFetchSize() > 1) {
                logger.error("incorrect fetch result {}", id);
            }
            while (rs.next()) {
                TodoInfo todo = new TodoInfo();
                todo.setTitle(rs.getString("TITLE"));
                todo.setCompleted(rs.getBoolean("COMPLETED"));
                todo.setOrder(rs.getInt("ORDER_ID"));
                 todoMap.put(rs.getString("id"), todo);
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

        return todoMap;
    }

    @Override
    public Map<String, TodoInfo> save(String id, TodoInfo todo) {
        try (final Connection connection = dataSource.getConnection()){
            String psInsert = "INSERT INTO TODO (ID, TITLE, COMPLETED, ORDER_ID) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(psInsert);
            stmt.setString(1, id);
            stmt.setString(2, todo.getTitle());
            stmt.setBoolean(3, todo.isCompleted());
            stmt.setInt(4, todo.getOrder());
            int count = stmt.executeUpdate();
            if (count != 1) {
                logger.error("Failed to insert TODO: {}", id);
            } else {
                Map<String, TodoInfo> todoMap = new HashMap<String, TodoInfo>();
                todoMap.put(id, todo);
                return todoMap;
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

        return null;
    }

    @Override
    public void remove(String id) {
        try (final Connection connection = dataSource.getConnection()){
            String psDelete = "UPDATE TODO SET ACTIVE_FLG = 'N' WHERE ID = ?";
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
