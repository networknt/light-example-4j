package com.networknt.example.sagas.ordersandcustomers.order.domain;

import com.networknt.example.sagas.ordersandcustomers.commondomain.Money;
import com.networknt.example.sagas.ordersandcustomers.order.common.OrderDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class OrderRepositoryJdbc implements OrderRepository {
    private static Logger logger = LoggerFactory.getLogger(OrderRepositoryJdbc.class);
    private static AtomicLong atomicOrderId = new AtomicLong(0);


    private DataSource dataSource;

    public OrderRepositoryJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Order save(Order order) {

        order.setId(atomicOrderId.incrementAndGet());


        String psInsert = "INSERT INTO customerorder.order_detail (order_id, customer_Id, state, amount) VALUES (?, ?, ?, ?)";

        try (final Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(psInsert);
            stmt.setLong(1, order.getId());
            stmt.setLong(2, order.getOrderDetails().getCustomerId());
            stmt.setString(3, order.getState().name());
            stmt.setBigDecimal(4, order.getOrderDetails().getOrderTotal().getAmount());
            int count = stmt.executeUpdate();
            if (count != 1) {
                logger.error("Failed to insert order: {}", order.getId());
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

        return order;

    }

    @Override
    public Order findOne(Long id) {

        String psSelect = "SELECT order_id,  customer_Id,state, amount from customerorder.order_detail WHERE order_id = ?";
        Order order = null;
        try (final Connection connection = dataSource.getConnection()){
            PreparedStatement stmt = connection.prepareStatement(psSelect);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs == null || rs.getFetchSize() > 1) {
                logger.error("incorrect fetch result {}", id);
            }
            while (rs.next()) {
                Money money = new Money ( rs.getBigDecimal("amount"));
                OrderDetails orderDetail = new OrderDetails(rs.getLong("customer_Id"), money);
                order = new Order(orderDetail);
                order.setId(id);
                order.setState(OrderState.valueOf(rs.getString("state")));
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }

        return order;
    }

    @Override
    public boolean exists(Long id) {
        Order order = findOne(id);
        if (order == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Map<Long, Order> findAll() {
        Map<Long, Order> orders = new HashMap<>();
        String psSelect = "SELECT order_id,  customer_Id,state, amount from customerorder.order_detail ";
        Order order = null;
        try (final Connection connection = dataSource.getConnection()){
            PreparedStatement stmt = connection.prepareStatement(psSelect);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Money money = new Money ( rs.getBigDecimal("amount"));
                OrderDetails orderDetail = new OrderDetails(rs.getLong("customer_Id"), money);
                order = new Order(orderDetail);
                order.setId(rs.getLong("order_Id"));
                order.setState(OrderState.valueOf(rs.getString("state")));
                orders.put(order.getId(), order);
            }
        } catch (SQLException e) {
            logger.error("SqlException:", e);
        }
        return orders;
    }

    @Override
    public long count() {
        return findAll().size();
    }

    @Override
    public void delete(Long id) {
       //TODO
    }

    @Override
    public void delete(Order entity) {
        //TODO
    }

    @Override
    public void deleteAll() {
        //TODO
    }
}
