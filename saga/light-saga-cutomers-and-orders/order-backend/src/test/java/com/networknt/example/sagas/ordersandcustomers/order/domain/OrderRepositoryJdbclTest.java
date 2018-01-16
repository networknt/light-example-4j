package com.networknt.example.sagas.ordersandcustomers.order.domain;


import com.networknt.example.sagas.ordersandcustomers.commondomain.Money;
import com.networknt.example.sagas.ordersandcustomers.order.common.OrderDetails;
import com.networknt.service.SingletonServiceFactory;
import org.h2.tools.RunScript;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Junit test class for UserRepositoryImpl.
 * use H2 test database for data source
 */
public class OrderRepositoryJdbclTest {

    public static DataSource ds;

    static {
        ds = (DataSource) SingletonServiceFactory.getBean(DataSource.class);
       try (Connection connection = ds.getConnection()) {
            // Runscript doesn't work need to execute batch here.
            String schemaResourceName = "/order_ddl.sql";
            InputStream in = OrderRepositoryJdbclTest.class.getResourceAsStream(schemaResourceName);

            if (in == null) {
                throw new RuntimeException("Failed to load resource: " + schemaResourceName);
            }
            InputStreamReader reader = new InputStreamReader(in);
            RunScript.execute(connection, reader);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private OrderRepository orderRepository = (OrderRepository) SingletonServiceFactory.getBean(OrderRepository.class);




    @BeforeClass
    public static void setUp() {

    }

    @Test
    public void testSave() {
        Money money = new Money (20);
        OrderDetails orderDetails = new OrderDetails(122L, money);
        Order order = new Order(orderDetails);
        order = orderRepository.save(order);
        assertTrue(order.getId()>0);
    }

    @Test
    public void testFindOne() {
        Money money = new Money (20);
        OrderDetails orderDetails = new OrderDetails(122L, money);
        Order order = new Order(orderDetails);
        order = orderRepository.save(order);
        Order result = orderRepository.findOne(order.getId());
        assertNotNull(result);
    }

}
