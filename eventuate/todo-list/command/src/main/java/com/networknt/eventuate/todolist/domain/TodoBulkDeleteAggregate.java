package com.networknt.eventuate.todolist.domain;


import com.networknt.eventuate.common.Event;
import com.networknt.eventuate.common.ReflectiveMutableCommandProcessingAggregate;
import com.networknt.eventuate.todolist.command.DeleteTodosCommand;
import com.networknt.eventuate.todolist.command.TodoCommand;
import com.networknt.eventuate.todolist.common.event.TodoDeletionRequestedEvent;

import java.util.List;
import java.util.stream.Collectors;


public class TodoBulkDeleteAggregate extends ReflectiveMutableCommandProcessingAggregate<TodoBulkDeleteAggregate, TodoCommand> {

    public List<Event> process(DeleteTodosCommand cmd) {
        return cmd.getIds()
                .stream()
                .map(TodoDeletionRequestedEvent::new)
                .collect(Collectors.toList());
    }

    public void apply(TodoDeletionRequestedEvent event) {

    }
}
