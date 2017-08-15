package com.networknt.eventuate.queryservice.customer;


import java.util.List;


public class CustomersQueryResponse {

  private List<QuerySideCustomer> customers;

  public CustomersQueryResponse() {
  }

  public CustomersQueryResponse(List<QuerySideCustomer> customers) {
    this.customers = customers;
  }

  public List<QuerySideCustomer> getCustomers() {
    return customers;
  }

  public void setCustomers(List<QuerySideCustomer> customers) {
    this.customers = customers;
  }
}
