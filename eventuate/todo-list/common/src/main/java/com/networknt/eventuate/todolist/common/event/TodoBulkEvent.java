package com.networknt.eventuate.todolist.common.event;


import com.networknt.eventuate.common.Event;
import com.networknt.eventuate.common.EventEntity;

@EventEntity(entity = "com.networknt.eventuate.todolist.domain.TodoBulkDeleteAggregate")
public interface TodoBulkEvent extends Event {
}
