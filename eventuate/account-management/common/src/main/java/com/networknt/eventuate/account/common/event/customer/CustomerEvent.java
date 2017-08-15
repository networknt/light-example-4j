package com.networknt.eventuate.account.common.event.customer;


import com.networknt.eventuate.common.Event;
import com.networknt.eventuate.common.EventEntity;

@EventEntity(entity = "com.networknt.eventuate.account.command.customer.Customer")
public interface CustomerEvent extends Event {
}