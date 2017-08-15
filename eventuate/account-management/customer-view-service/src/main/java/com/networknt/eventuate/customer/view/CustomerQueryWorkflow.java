package com.networknt.eventuate.customer.view;


import com.networknt.eventuate.account.common.event.account.AccountDeletedEvent;
import com.networknt.eventuate.account.common.event.customer.CustomerAddedToAccount;
import com.networknt.eventuate.account.common.event.customer.CustomerCreatedEvent;
import com.networknt.eventuate.account.common.event.customer.CustomerToAccountDeleted;
import com.networknt.eventuate.account.common.model.customer.ToAccountInfo;
import com.networknt.eventuate.common.DispatchedEvent;
import com.networknt.eventuate.common.EventHandlerMethod;
import com.networknt.eventuate.common.EventSubscriber;
import com.networknt.eventuate.queryservice.customer.CustomerInfoUpdateService;
import com.networknt.service.SingletonServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@EventSubscriber(id = "customerQuerySideEventHandlers")
public class CustomerQueryWorkflow {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private CustomerInfoUpdateService customerInfoUpdateService =
          (CustomerInfoUpdateService) SingletonServiceFactory.getBean(CustomerInfoUpdateService.class);

  public CustomerQueryWorkflow() {

  }

  @EventHandlerMethod
  public void create(DispatchedEvent<CustomerCreatedEvent> de) {
    CustomerCreatedEvent event = de.getEvent();
    String id = de.getEntityId();

    customerInfoUpdateService.create(id, event.getCustomerInfo());
  }

  @EventHandlerMethod
  public void addToAccount(DispatchedEvent<CustomerAddedToAccount> de) {
    CustomerAddedToAccount event = de.getEvent();
    String id = de.getEntityId();

    ToAccountInfo toAccountInfo = event.getToAccountInfo();

    customerInfoUpdateService.addToAccount(id, toAccountInfo);
  }

  @EventHandlerMethod
  public void deleteToAccount(DispatchedEvent<CustomerToAccountDeleted> de) {
    String id = de.getEntityId();
    CustomerToAccountDeleted event = de.getEvent();
    String accountId = event.getAccountId();

    customerInfoUpdateService.deleteToAccount(id, accountId);
  }

  @EventHandlerMethod
  public void deleteToAccounts(DispatchedEvent<AccountDeletedEvent> de) {
    String accountId = de.getEntityId();

    customerInfoUpdateService.deleteToAccountFromAllCustomers(accountId);
  }
}
