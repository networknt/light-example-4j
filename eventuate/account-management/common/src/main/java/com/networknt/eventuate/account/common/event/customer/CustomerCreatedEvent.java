package com.networknt.eventuate.account.common.event.customer;


import com.networknt.eventuate.account.common.model.customer.CustomerInfo;

public class CustomerCreatedEvent implements CustomerEvent {

  private CustomerInfo customerInfo;

  public CustomerCreatedEvent() {
  }

  public CustomerCreatedEvent(CustomerInfo customerInfo) {
    this.customerInfo = customerInfo;
  }

  public CustomerInfo getCustomerInfo() {
    return customerInfo;
  }

  public void setCustomerInfo(CustomerInfo customerInfo) {
    this.customerInfo = customerInfo;
  }
}
