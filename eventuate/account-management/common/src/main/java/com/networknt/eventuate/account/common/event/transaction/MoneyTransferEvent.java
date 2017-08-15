package com.networknt.eventuate.account.common.event.transaction;


import com.networknt.eventuate.common.Event;
import com.networknt.eventuate.common.EventEntity;

@EventEntity(entity="com.networknt.eventuate.account.command.transaction.MoneyTransfer")
public interface MoneyTransferEvent extends Event {
}
