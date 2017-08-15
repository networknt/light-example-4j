package com.networknt.eventuate.queryservice.customer;


import com.networknt.eventuate.account.common.model.customer.Address;
import com.networknt.eventuate.account.common.model.customer.Name;
import com.networknt.eventuate.common.Int128;
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

/**
 * Junit test class for UserRepositoryImpl.
 * use H2 test database for data source
 */
public class CustomerViewRepositoryImplTest {

    public static DataSource ds;

    static {
        ds = (DataSource) SingletonServiceFactory.getBean(DataSource.class);
       try (Connection connection = ds.getConnection()) {
            // Runscript doesn't work need to execute batch here.
            String schemaResourceName = "/queryside_ddl.sql";
            InputStream in = CustomerViewRepositoryImplTest.class.getResourceAsStream(schemaResourceName);

            if (in == null) {
                throw new RuntimeException("Failed to load resource: " + schemaResourceName);
            }
            InputStreamReader reader = new InputStreamReader(in);
            RunScript.execute(connection, reader);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private CustomerViewRepository customerRepository = (CustomerViewRepository) SingletonServiceFactory.getBean(CustomerViewRepository.class);

    private static QuerySideCustomer customer;
    private static  String  id;
    @BeforeClass
    public static void setUp() {

        Int128 idGen = new Int128(1222L, 1011L);
        id = idGen.asString();
        Name name = new Name("Google", "Com");
        Address address = new Address("Yonge St" , "2556 unit", "toronto", "ON", "Canada", "L3R, 5F5");
        customer = new QuerySideCustomer(id, name, "aaa.bbb.@gmail.com", "1222", "222222222", "415-555-5555", address, null);

    }

    @Test
    public void testSave() {
        customerRepository.save(customer);
    }

    @Test
    public void testFindOneCustomer() {
        QuerySideCustomer result = customerRepository.findOneCustomer(id);
        assertNotNull(result);

    }



    @Test
    public void testFindByEmail() {
        List<QuerySideCustomer> result = customerRepository.findByEmailLike("%aaa%");
        assertNotNull(result);
      //  System.out.println("customer name:" + result.get(0).getName().getFirstName());

    }



}
