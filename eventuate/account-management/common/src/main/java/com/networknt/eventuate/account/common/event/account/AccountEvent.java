package com.networknt.eventuate.account.common.event.account;


import com.networknt.eventuate.common.Event;
import com.networknt.eventuate.common.EventEntity;

@EventEntity(entity="com.networknt.eventuate.account.command.account.Account")
public interface AccountEvent extends Event {
}
