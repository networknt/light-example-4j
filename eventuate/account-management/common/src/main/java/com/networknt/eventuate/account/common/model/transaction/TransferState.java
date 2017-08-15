package com.networknt.eventuate.account.common.model.transaction;

public enum TransferState {
  NEW, INITIAL, DEBITED, COMPLETED, FAILED_DUE_TO_INSUFFICIENT_FUNDS
}
