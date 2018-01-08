package com.networknt.example.sagas.ordersandcustomers.customer.service;


import com.networknt.example.sagas.ordersandcustomers.commondomain.Money;
import com.networknt.example.sagas.ordersandcustomers.customer.domain.Customer;
import com.networknt.example.sagas.ordersandcustomers.customer.domain.CustomerRepository;

public class CustomerService {

  private CustomerRepository customerRepository;

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public Customer createCustomer(String name, Money creditLimit) {
    Customer customer  = new Customer(name, creditLimit);
    return (Customer)customerRepository.save(customer);
  }
}
