package com.networknt.eventuate.queryservice.account;


import com.networknt.eventuate.account.common.model.account.AccountChangeInfo;
import com.networknt.eventuate.account.common.model.account.AccountTransactionInfo;
import com.networknt.eventuate.account.common.model.transaction.TransferState;

import java.util.List;

interface AccountInfoRepository {

    List<AccountInfo> findByCustomerId(String customerId);

    AccountInfo findOneAccount(String accountId);

    List<AccountTransactionInfo> getAccountTransactionHistory(String accountId);


    void delete(String accountId);

    int createAccount(AccountInfo accountInfo);

    int addTransaction(AccountTransactionInfo ti);

    int updateTransactionStatus(String transactionId, TransferState status);

    int updateBalance(String accountId, String changeId, long balanceDelta, AccountChangeInfo ci);

}
