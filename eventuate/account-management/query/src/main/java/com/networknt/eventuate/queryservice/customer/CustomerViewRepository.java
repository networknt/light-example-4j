package com.networknt.eventuate.queryservice.customer;



import com.networknt.eventuate.account.common.model.customer.ToAccountInfo;

import java.util.List;

interface CustomerViewRepository  {

  List<QuerySideCustomer> findByEmailLike(String email);

  QuerySideCustomer findOneCustomer(String customerId);

  void save(QuerySideCustomer customer);

  int addToAccount(String id, ToAccountInfo accountInfo);

  void deleteToAccount(String id, String accountId);

  void deleteToAccountFromAllCustomers(String accountId);
}
