package com.networknt.eventuate.queryservice.account;

import com.networknt.eventuate.account.common.model.account.AccountTransactionInfo;

import java.util.List;

public class AccountQueryService {

  private AccountInfoRepository accountInfoRepository;

  public AccountQueryService(AccountInfoRepository accountInfoRepository) {
    this.accountInfoRepository = accountInfoRepository;
  }

  public AccountInfo findByAccountId(String accountId) {
    AccountInfo account = accountInfoRepository.findOneAccount(accountId);
    if (account == null) {
      //TODO handle account not exception here
      return account;
      //throw new AccountNotFoundException(accountId);
    }
    else
      return account;
  }

  public List<AccountInfo> findByCustomerId(String customerId) {
      return accountInfoRepository.findByCustomerId(customerId);
  }

  public List<AccountTransactionInfo> getAccountTransactionHistory(String accountId) {
    return accountInfoRepository.getAccountTransactionHistory(accountId);
  }
}
