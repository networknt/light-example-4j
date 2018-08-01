package com.networknt.example.sagas.ordersandcustomers.customer.domain;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.networknt.example.sagas.ordersandcustomers.commondomain.Money;
import com.networknt.example.sagas.ordersandcustomers.util.IdentityGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class CustomerRepositoryJdbc implements CustomerRepository {

    private static Logger logger = LoggerFactory.getLogger(CustomerRepositoryJdbc.class);

    private DataSource dataSource;

    public CustomerRepositoryJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public Customer save(Customer customer) {

        customer.setId(IdentityGenerator.generate());

        String psInsert = "INSERT INTO customerorder.customer (customer_Id, name, creditLimit) VALUES (?, ?, ?)";

        try (final Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(psInsert)) {
            stmt.setLong(1, customer.getId());
            stmt.setString(2, customer.getName());
            stmt.setBigDecimal(3, customer.getCreditLimit());

            int count = stmt.executeUpdate();
            if (count != 1) {
                logger.error("Failed to insert Customer: {}", customer.getId());
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

        return customer;
    }

    @Override
    public Customer findOne(Long id) {

        String psSelect = "SELECT customer_Id,name, creditLimit from customerorder.customer WHERE customer_id = ?";

        Customer customer = null;
        try (final Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(psSelect)){
            stmt.setLong(1, id);
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs == null || rs.getFetchSize() > 1) {
                    logger.error("incorrect fetch result {}", id);
                }
                while (rs.next()) {
                    Money money = new Money ( rs.getBigDecimal("creditLimit"));
                    customer = new Customer(rs.getString("name"), money);

                    customer.setCreditReservations(creditReservations(id));
                }
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

        return customer;
    }

    public  Map<Long, Money> creditReservations(Long id) {
        Map<Long, Money> creditReservations = new HashMap<>();
        String psSelect = "SELECT customer_Id,order_id, orderTotal from customerorder.customer_order WHERE customer_id = ?";
        try (final Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(psSelect)){
            stmt.setLong(1, id);
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Money money = new Money ( rs.getBigDecimal("orderTotal"));
                    creditReservations.put(rs.getLong("order_id"), money);
                }
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }
        return creditReservations;
    }

    @Override
    public boolean exists(Long id) {
        Customer customer =findOne(id);
        if (customer == null) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    public Map<Long, Customer> findAll() {
        Map<Long, Customer> customers = new HashMap<>();

        String psSelect = "SELECT customer_Id,name, creditLimit from customerorder.customer";
        try (final Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(psSelect); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                Money money = new Money ( rs.getBigDecimal("creditLimit"));
                Customer customer = new Customer(rs.getString("name"), money);
                customers.put(customer.getId(), customer);
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }


        return customers;
    }


    @Override
    public long count() {
        return findAll().size();
    }

    @Override
    public Customer update(Customer customer) {

        customer.setId(IdentityGenerator.generate());

        String psDelete = "DELETE FROM  customerorder.customer_order WHERE customer_id = ?";
        String psInsert = "INSERT INTO customerorder.customer_order (customer_Id, order_id, orderTotal) VALUES (?, ?, ?)";

        try (final Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(psDelete)) {
            connection.setAutoCommit(false);
            stmt.setLong(1, customer.getId());
            stmt.executeUpdate();
            if (customer.getCreditReservations().size()>0) {
                for (Map.Entry<Long, Money> entry : customer.getCreditReservations().entrySet()) {
                    PreparedStatement stmt2 = connection.prepareStatement(psInsert);
                    stmt2.setLong(1, customer.getId());
                    stmt2.setLong(2, entry.getKey());
                    stmt2.setBigDecimal(3, entry.getValue().getAmount());
                    stmt2.executeUpdate();
                }
            }

            connection.commit();
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

        return customer;
    }

    @Override
    public void delete(Long id) {
         //TODO
    }



    @Override
    public void deleteAll() {
        //TODO
    }
}
