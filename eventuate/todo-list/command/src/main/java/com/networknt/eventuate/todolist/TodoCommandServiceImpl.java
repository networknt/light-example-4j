package com.networknt.eventuate.todolist;

import com.networknt.eventuate.common.AggregateRepository;
import com.networknt.eventuate.common.EntityWithIdAndVersion;
import com.networknt.eventuate.common.EventuateAggregateStore;
import com.networknt.eventuate.todolist.command.*;
import com.networknt.eventuate.todolist.common.model.TodoInfo;
import com.networknt.eventuate.todolist.domain.TodoAggregate;
import com.networknt.eventuate.todolist.domain.TodoBulkDeleteAggregate;
import com.networknt.service.SingletonServiceFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Created by stevehu on 2016-12-10.
 */
public class TodoCommandServiceImpl implements TodoCommandService {

    private AggregateRepository<TodoAggregate, TodoCommand> aggregateRepository;
    private  AggregateRepository<TodoBulkDeleteAggregate, TodoCommand> bulkDeleteAggregateRepository;

    public TodoCommandServiceImpl(AggregateRepository<TodoAggregate, TodoCommand> todoRepository, AggregateRepository<TodoBulkDeleteAggregate, TodoCommand> bulkDeleteAggregateRepository) {
        this.aggregateRepository = todoRepository;
        this.bulkDeleteAggregateRepository = bulkDeleteAggregateRepository;
    }


    @Override
    public CompletableFuture<EntityWithIdAndVersion<TodoAggregate>> add(TodoInfo todo) {
        return aggregateRepository.save(new CreateTodoCommand(todo));
    }

    @Override
    public CompletableFuture<EntityWithIdAndVersion<TodoAggregate>> remove(String id) {
        return aggregateRepository.update(id, new DeleteTodoCommand());
    }

    @Override
    public CompletableFuture<EntityWithIdAndVersion<TodoAggregate>> update(String id, TodoInfo newTodo) {
        return aggregateRepository.update(id, new UpdateTodoCommand(id, newTodo));
    }

    @Override
    public CompletableFuture<EntityWithIdAndVersion<TodoBulkDeleteAggregate>>  deleteAll(List<String> ids) {
        return bulkDeleteAggregateRepository.save(new DeleteTodosCommand(ids));

    }
}
