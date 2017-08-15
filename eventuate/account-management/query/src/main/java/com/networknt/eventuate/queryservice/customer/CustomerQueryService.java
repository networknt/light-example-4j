package com.networknt.eventuate.queryservice.customer;


import com.networknt.eventuate.common.CompletableFutureUtil;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CustomerQueryService {

  private CustomerViewRepository querySideCustomerRepository;

  public CustomerQueryService(CustomerViewRepository querySideCustomerRepository) {
    this.querySideCustomerRepository = querySideCustomerRepository;
  }

  public CompletableFuture<QuerySideCustomer> findByCustomerId(String customerId) {
    QuerySideCustomer customer = querySideCustomerRepository.findOneCustomer(customerId);
    if (customer == null)
      return CompletableFutureUtil.failedFuture(new Exception("Empty result Exception"));
    else
      return CompletableFuture.completedFuture(customer);
  }

  public CompletableFuture<List<QuerySideCustomer>> findByEmail(String email) {
    List<QuerySideCustomer> customers = querySideCustomerRepository.findByEmailLike(email);
    if (customers.isEmpty())
      return CompletableFutureUtil.failedFuture(new  Exception("Empty result Exception"));
    else
      return CompletableFuture.completedFuture(customers);
  }
}
