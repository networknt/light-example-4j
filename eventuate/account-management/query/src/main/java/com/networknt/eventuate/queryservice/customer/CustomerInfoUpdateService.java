package com.networknt.eventuate.queryservice.customer;


import com.networknt.eventuate.account.common.model.customer.CustomerInfo;
import com.networknt.eventuate.account.common.model.customer.ToAccountInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;


public class CustomerInfoUpdateService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private CustomerViewRepository querySideCustomerRepository;

  public CustomerInfoUpdateService(CustomerViewRepository querySideCustomerRepository) {
    this.querySideCustomerRepository = querySideCustomerRepository;

  }

  public void create(String id, CustomerInfo customerInfo) {
    try {
      querySideCustomerRepository.save(new QuerySideCustomer(id,
                      customerInfo.getName(),
                      customerInfo.getUserCredentials().getEmail(),
                      customerInfo.getUserCredentials().getPassword(),
                      customerInfo.getSsn(),
                      customerInfo.getPhoneNumber(),
                      customerInfo.getAddress(),
                      Collections.<String, ToAccountInfo>emptyMap()
              )
      );
      logger.info("Saved in mongo");
    } catch (Exception t) {
      logger.warn("When saving ", t);
    }
  }

  public void addToAccount(String id, ToAccountInfo accountInfo) {
    int rec = querySideCustomerRepository.addToAccount(id,accountInfo );
    if (rec<1) {
      logger.error("fail to attach account the customer");
    }

  }

  public void deleteToAccountFromAllCustomers(String accountId) {
    querySideCustomerRepository.deleteToAccountFromAllCustomers(accountId);
  }

  public void deleteToAccount(String customerId, String accountId) {
    querySideCustomerRepository.deleteToAccount(customerId, accountId);
  }
}
