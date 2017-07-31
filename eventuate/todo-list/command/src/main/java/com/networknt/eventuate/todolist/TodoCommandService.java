package com.networknt.eventuate.todolist;

import com.networknt.eventuate.common.EntityWithIdAndVersion;
import com.networknt.eventuate.todolist.common.model.TodoInfo;
import com.networknt.eventuate.todolist.domain.TodoAggregate;
import com.networknt.eventuate.todolist.domain.TodoBulkDeleteAggregate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by stevehu on 2016-12-10.
 */
public interface TodoCommandService {

    CompletableFuture<EntityWithIdAndVersion<TodoAggregate>> add(TodoInfo todo);

    CompletableFuture<EntityWithIdAndVersion<TodoAggregate>> remove(String id);

    CompletableFuture<EntityWithIdAndVersion<TodoAggregate>> update(String id, TodoInfo newTodo);

    CompletableFuture<EntityWithIdAndVersion<TodoBulkDeleteAggregate>>  deleteAll(List<String> ids);
}
