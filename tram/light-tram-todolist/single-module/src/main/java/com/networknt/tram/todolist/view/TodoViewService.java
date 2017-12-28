package com.networknt.tram.todolist.view;


import java.util.List;


public interface TodoViewService {




  public List<TodoView> search(String value);


  public void index(TodoView todoView) ;


  public void remove(String id) ;

}
