package com.networknt.eventuate.todolist;


import com.networknt.eventuate.common.CompletableFutureUtil;
import com.networknt.eventuate.todolist.common.model.TodoInfo;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;


public class TodoQueryServiceImpl implements TodoQueryService {

    private TodoQueryRepository todoQueryRepository;

    public TodoQueryServiceImpl(TodoQueryRepository todoQueryRepository) {
        this.todoQueryRepository = todoQueryRepository;
    }

    @Override
    public Map<String, TodoInfo> save(String id, TodoInfo todo) {
        return todoQueryRepository.save(id, todo);
    }

    @Override
    public void remove(String id) {
        todoQueryRepository.remove(id);
    }


    @Override
    public List<Map<String, TodoInfo>> getAll() {
        return todoQueryRepository.getAll();
    }

    @Override
    public CompletableFuture<Map<String, TodoInfo>> findById(String id) {
        Map<String, TodoInfo> res = todoQueryRepository.findById(id);
        if (res != null) {
            return CompletableFuture.completedFuture(res);
        }
        return CompletableFutureUtil.failedFuture(new NoSuchElementException("No todo with given id found"));
    }
}
