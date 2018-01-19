package com.networknt.example.sagas.ordersandcustomers.order.domain;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class MapOrderRepository implements OrderRepository {
    private static Logger logger = LoggerFactory.getLogger(MapOrderRepository.class);
    private static AtomicLong atomicOrderId = new AtomicLong(0);

    private static Integer expiredInMinutes = 10 * 60;

    static Cache<Long, Order> orders;
    static {
        orders = Caffeine.newBuilder()
                // assuming that the clock screw time is less than 5 minutes
                .expireAfterWrite(expiredInMinutes, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public Order save(Order order) {
        order.setId(atomicOrderId.incrementAndGet());
        this.orders.put(order.getId(), order);
        return order;

    }

    @Override
    public Order findOne(Long id) {
        Order order = this.orders.getIfPresent(id);
        return order;
    }

    @Override
    public boolean exists(Long id) {
        Order order = this.orders.getIfPresent(id);
        if (order == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Map<Long, Order> findAll() {
        return orders.asMap();
    }

    @Override
    public long count() {
        return orders.asMap().size();
    }

    @Override
    public void delete(Long id) {
        Order order = this.orders.getIfPresent(id);
        orders.invalidate(order);
    }

    @Override
    public void delete(Order entity) {
        orders.invalidate(entity);
    }

    @Override
    public void deleteAll() {
        orders.invalidateAll();
    }

    @Override
    public Order update(Order order) {
        this.orders.put(order.getId(), order);
        return    order;

    }

}
