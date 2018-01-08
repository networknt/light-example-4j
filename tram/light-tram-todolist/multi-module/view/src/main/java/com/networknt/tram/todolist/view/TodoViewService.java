package com.networknt.tram.todolist.view;

import java.util.List;

public interface TodoViewService {

  List<TodoView> search(String value);

  void index(TodoView todoView) ;

  void remove(String id) ;

}
