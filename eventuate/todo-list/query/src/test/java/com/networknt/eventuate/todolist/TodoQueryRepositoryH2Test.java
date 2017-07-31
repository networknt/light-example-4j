package com.networknt.eventuate.todolist;

import com.networknt.eventuate.common.Int128;
import com.networknt.eventuate.todolist.common.model.TodoInfo;
import com.networknt.service.SingletonServiceFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.h2.tools.RunScript;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by gavin on 2017-04-24.
 * This is a sample test for H2 test DB.
 * refer the
 */
public class TodoQueryRepositoryH2Test {

    public static DataSource ds;

    static {
        ds = (DataSource) SingletonServiceFactory.getBean(DataSource.class);
        try (Connection connection = ds.getConnection()) {
            // Runscript doesn't work need to execute batch here.
        String schemaResourceName = "/todolist-example-h2-ddl.sql";
        InputStream in = TodoQueryRepositoryH2Test.class.getResourceAsStream(schemaResourceName);

        if (in == null) {
            throw new RuntimeException("Failed to load resource: " + schemaResourceName);
        }
        InputStreamReader reader = new InputStreamReader(in);
        RunScript.execute(connection, reader);

    } catch (SQLException e) {
        e.printStackTrace();
    }
    }
    private TodoQueryRepository todoQueryRepository = (TodoQueryRepository)SingletonServiceFactory.getBean(TodoQueryRepository.class);
    private static  TodoInfo todo;
    private static  String  id;
    @BeforeClass
    public static void setUp() {
        todo = new TodoInfo();
        todo.setOrder(1);
        todo.setTitle("complete the test first");
        todo.setCompleted(false);

        Int128 idGen = new Int128(1222L, 1011L);
        id = idGen.asString();
    }

    @Test
    public void testSave() {
        Map<String, TodoInfo>  result = todoQueryRepository.save(id, todo);
        assertNotNull(result);
    }

    @Test
    public void testGetAll() {
        List<Map<String, TodoInfo>> result= todoQueryRepository.getAll();
        assertTrue(result.size()>0);
    }

    @Test
    public void testFindById() {
        Map<String, TodoInfo> result = todoQueryRepository.findById(id);
        assertTrue(result.size()>0);

    }


    @Test
    public void testRemove() {
         todoQueryRepository.remove(id);
        Map<String, TodoInfo> result = todoQueryRepository.findById(id);
        assertTrue(result.size() ==0);
    }
}
