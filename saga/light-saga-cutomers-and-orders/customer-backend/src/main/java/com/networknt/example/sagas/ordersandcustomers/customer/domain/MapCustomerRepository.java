package com.networknt.example.sagas.ordersandcustomers.customer.domain;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;


public class MapCustomerRepository implements CustomerRepository {

    private static Logger logger = LoggerFactory.getLogger(MapCustomerRepository.class);

    private static Integer expiredInMinutes = 10 * 60;
    static Cache<Long, Customer> customers;
    static {
        customers = Caffeine.newBuilder()
                // assuming that the clock screw time is less than 5 minutes
                .expireAfterWrite(expiredInMinutes, TimeUnit.MINUTES)
                .build();
    }


    @Override
    public Customer save(Customer customer) {
        customer.setId(Long.valueOf(customers.asMap().size()));
        this.customers.put(customer.getId(), customer);
        return customer;
    }

    @Override
    public Customer findOne(Long id) {
        Customer customer = this.customers.getIfPresent(id);
        return customer;
    }

    @Override
    public boolean exists(Long id) {
        Customer customer = this.customers.getIfPresent(id);
        if (customer == null) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    public Map<Long, Customer> findAll() {
        return customers.asMap();
    }


    @Override
    public long count() {
        return customers.asMap().size();
    }

    @Override
    public void delete(Long id) {
        Customer customer = this.customers.getIfPresent(id);
        customers.invalidate(customer);
    }



    @Override
    public void deleteAll() {
        customers.invalidateAll();
    }
}
