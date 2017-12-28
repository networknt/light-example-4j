package com.networknt.tram.todolist.view;

/*
 *  test view service, for the test, please start elasticsearch docker image
 *  on the project root filer run : docker-compose up
 */

import com.networknt.service.SingletonServiceFactory;
import org.junit.Test;


public class ViewModuleTest {


  private TodoViewService todoViewService =  (TodoViewService)SingletonServiceFactory.getBean(TodoViewService.class);

  @Test
  public void testIndexSearchAndRemove() throws Exception {
  /*  String id = Utils.generateUniqueString();
    String title = "test";
    TodoView todoView = new TodoView(id, title, false, 0);

    todoViewService.index(todoView);
    Thread.sleep(2000); //Elasticsearch by default refreshes each shard every 1s
    List<TodoView> todoViews = todoViewService.search(title);
    Assert.assertTrue(todoViews.stream().anyMatch(view -> id.equals(view.getId())));

    todoViewService.remove(id);
    Thread.sleep(2000);
    todoViews = todoViewService.search(title);
    Assert.assertFalse(todoViews.stream().anyMatch(view -> id.equals(view.getId())));*/
  }
}
