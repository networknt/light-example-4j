package com.networknt.eventuate.todolist;

import com.networknt.eventuate.todolist.common.model.TodoInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public interface TodoQueryService {

    List<Map<String, TodoInfo>> getAll();

    CompletableFuture<Map<String, TodoInfo>> findById(String id);

    Map<String, TodoInfo> save(String id, TodoInfo todo);

    void remove(String id);
}
