package com.networknt.eventuate.account.command.customer;


import com.networknt.eventuate.account.common.model.customer.CustomerInfo;

public class CreateCustomerCommand implements CustomerCommand {
  private CustomerInfo customerInfo;

  public CreateCustomerCommand(CustomerInfo customerInfo) {
    this.customerInfo = customerInfo;
  }

  public CustomerInfo getCustomerInfo() {
    return customerInfo;
  }
}
