package com.networknt.eventuate.queryservice.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.networknt.eventuate.account.common.model.account.AccountChangeInfo;
import com.networknt.eventuate.account.common.model.account.AccountTransactionInfo;

import java.math.BigDecimal;
import java.util.*;


public class AccountInfo {

  private String id;
  private String customerId;
  private String title;
  private String description;
  private long balance;
  private List<AccountChangeInfo> changes;
  private Map<String, AccountTransactionInfo> transactions;
  private String version;
  @JsonProperty("date")
  private Date creationDate;

  public AccountInfo() {
  }

  public AccountInfo(String id, String customerId, String title, String description, long balance, List<AccountChangeInfo> changes, Map<String, AccountTransactionInfo> transactions, String version) {
    this(id, customerId, title, description, balance, changes, transactions, version, new Date());
  }

  public AccountInfo(String id, String customerId, String title, String description, long balance, List<AccountChangeInfo> changes, Map<String, AccountTransactionInfo> transactions, String version, Date creationDate) {

    this.id = id;
    this.customerId = customerId;
    this.title = title;
    this.description = description;
    this.balance = balance;
    this.changes = changes;
    this.transactions = transactions;
    this.version = version;
    this.creationDate = creationDate;
  }

  public String getId() {
    return id;
  }

  public String getCustomerId() {
    return customerId;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public long getBalance() {
    return balance;
  }

  public List<AccountChangeInfo> getChanges() {
    return changes == null ? Collections.EMPTY_LIST : changes;
  }

  public List<AccountTransactionInfo> getTransactions() {
    return transactions == null ? Collections.EMPTY_LIST : new ArrayList<>(transactions.values());
  }

  public String getVersion() {
    return version;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setBalance(long balance) {
    this.balance = balance;
  }

  public void setChanges(List<AccountChangeInfo> changes) {
    this.changes = changes;
  }

  public void setTransactions(Map<String, AccountTransactionInfo> transactions) {
    this.transactions = transactions;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }
}
