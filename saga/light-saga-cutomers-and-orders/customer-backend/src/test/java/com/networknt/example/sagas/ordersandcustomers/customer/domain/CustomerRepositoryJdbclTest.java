package com.networknt.example.sagas.ordersandcustomers.customer.domain;


import com.networknt.eventuate.common.Int128;
import com.networknt.example.sagas.ordersandcustomers.commondomain.Money;
import com.networknt.service.SingletonServiceFactory;
import org.h2.tools.RunScript;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Junit test class for UserRepositoryImpl.
 * use H2 test database for data source
 */
public class CustomerRepositoryJdbclTest {

    public static DataSource ds;

    static {
        ds = (DataSource) SingletonServiceFactory.getBean(DataSource.class);
       try (Connection connection = ds.getConnection()) {
            // Runscript doesn't work need to execute batch here.
            String schemaResourceName = "/customer_ddl.sql";
            InputStream in = CustomerRepositoryJdbclTest.class.getResourceAsStream(schemaResourceName);

            if (in == null) {
                throw new RuntimeException("Failed to load resource: " + schemaResourceName);
            }
            InputStreamReader reader = new InputStreamReader(in);
            RunScript.execute(connection, reader);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private CustomerRepository customerRepository = (CustomerRepository) SingletonServiceFactory.getBean(CustomerRepository.class);




    @BeforeClass
    public static void setUp() {

    }

    @Test
    public void testSave() {
        Money money = new Money (200);
        Customer customer = new Customer("test customer", money);
        customer = customerRepository.save(customer);
        assertTrue(customer.getId()>0);
    }

    @Test
    public void testFindOne() {
        Money money = new Money (200);
        Customer customer = new Customer("test customer", money);
        customer = customerRepository.save(customer);
        Customer result = customerRepository.findOne(customer.getId());
        assertNotNull(result);
    }

}
