package com.networknt.eventuate.todolist;

import com.networknt.eventuate.todolist.common.model.TodoInfo;


import java.util.List;
import java.util.Map;


public interface TodoQueryRepository {

    List<Map<String, TodoInfo>> getAll();

    Map<String, TodoInfo> findById(String id);

    Map<String, TodoInfo> save(String id, TodoInfo todo);

    void remove(String id);
}
