package com.networknt.eventuate.account.command.customer;


import com.networknt.eventuate.account.common.event.customer.CustomerAddedToAccount;
import com.networknt.eventuate.account.common.event.customer.CustomerCreatedEvent;
import com.networknt.eventuate.account.common.event.customer.CustomerToAccountDeleted;
import com.networknt.eventuate.account.common.model.customer.CustomerInfo;
import com.networknt.eventuate.common.Event;
import com.networknt.eventuate.common.EventUtil;
import com.networknt.eventuate.common.ReflectiveMutableCommandProcessingAggregate;

import java.util.List;

public class Customer extends ReflectiveMutableCommandProcessingAggregate<Customer, CustomerCommand> {

  private CustomerInfo customerInfo;

  public List<Event> process(CreateCustomerCommand cmd) {
    return EventUtil.events(new CustomerCreatedEvent(cmd.getCustomerInfo()));
  }

  public List<Event> process(AddToAccountCommand cmd) {
    return EventUtil.events(new CustomerAddedToAccount(cmd.getToAccountInfo()));
  }
  public List<Event> process(DeleteToAccountCommand cmd) {
    return EventUtil.events(new CustomerToAccountDeleted(cmd.getAccountId()));
  }

  public void apply(CustomerCreatedEvent event) {
    customerInfo = event.getCustomerInfo();
  }

  public void apply(CustomerAddedToAccount event) {
  }
  public void apply(CustomerToAccountDeleted event) {
  }

  public CustomerInfo getCustomerInfo() {
    return customerInfo;
  }
}
