package com.networknt.tram.todolist.command;

import com.networknt.eventuate.jdbc.IdGenerator;
import com.networknt.eventuate.jdbc.IdGeneratorImpl;
import com.networknt.tram.event.common.DomainEvent;
import com.networknt.tram.event.publisher.DomainEventPublisher;
import com.networknt.tram.event.publisher.DomainEventPublisherImpl;
import com.networknt.tram.message.producer.MessageProducer;
import com.networknt.tram.message.producer.jdbc.MessageProducerJdbcConnectionImpl;
import com.networknt.tram.todolist.common.TodoCreated;
import com.networknt.tram.todolist.common.TodoDeleted;
import com.networknt.tram.todolist.common.TodoUpdated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static java.util.Arrays.asList;


public class TodoCommandService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private TodoRepository todoRepository;

  private DomainEventPublisher domainEventPublisher;

  private IdGenerator idGenerator;

  private DataSource dataSource;

  public TodoCommandService( DataSource dataSource,IdGenerator idGenerator) {
    this.dataSource = dataSource;
    this.idGenerator = idGenerator;
  }

  public Todo create(CreateTodoRequest createTodoRequest) {

      Todo todo = new Todo(createTodoRequest.getTitle(), createTodoRequest.isCompleted(), createTodoRequest.getOrder());
      try (final Connection connection = dataSource.getConnection()) {
          connection.setAutoCommit(false);
          this.todoRepository = new TodoRepositoryImpl(connection);
          MessageProducer messageProducer = new MessageProducerJdbcConnectionImpl(connection,idGenerator);
          this.domainEventPublisher = new DomainEventPublisherImpl(messageProducer);

          todo = todoRepository.save(todo);
          publishTodoEvent(todo, new TodoCreated(todo.getTitle(), todo.isCompleted(), todo.getExecutionOrder()));

          connection.commit();
      } catch (SQLException e) {
          logger.error("SqlException:", e);
      }

      return todo;
  }

  private void publishTodoEvent(Todo todo, DomainEvent... domainEvents) {
    domainEventPublisher.publish(Todo.class, todo.getId(), asList(domainEvents));
  }

  private void publishTodoEvent(String id, DomainEvent... domainEvents) {
    domainEventPublisher.publish(Todo.class, id, asList(domainEvents));
  }

  public Todo update(String id, UpdateTodoRequest updateTodoRequest) throws TodoNotFoundException {
      try (final Connection connection = dataSource.getConnection()) {
          connection.setAutoCommit(false);
          this.todoRepository = new TodoRepositoryImpl(connection);
          Todo todo = todoRepository.findOne(id);
          if (todo == null) {
              throw new TodoNotFoundException(id);
          }
          todo.setTitle(updateTodoRequest.getTitle());
          todo.setCompleted(updateTodoRequest.isCompleted());
          todo.setExecutionOrder(updateTodoRequest.getOrder());
          todoRepository.update(todo);

          MessageProducer messageProducer = new MessageProducerJdbcConnectionImpl(connection, new IdGeneratorImpl());
          this.domainEventPublisher = new DomainEventPublisherImpl(messageProducer);
          publishTodoEvent(todo, new TodoUpdated(todo.getTitle(), todo.isCompleted(), todo.getExecutionOrder()));

          connection.commit();

          return todo;
      } catch (SQLException e) {
          logger.error("SqlException:", e);
      }

    return null;
  }

  public void delete(String id) {
      try (final Connection connection = dataSource.getConnection()) {
          this.todoRepository = new TodoRepositoryImpl(connection);
          MessageProducer messageProducer = new MessageProducerJdbcConnectionImpl(connection,idGenerator);
          this.domainEventPublisher = new DomainEventPublisherImpl(messageProducer);

          todoRepository.delete(id);
          publishTodoEvent(id, new TodoDeleted());

      } catch (SQLException e) {
          logger.error("SqlException:", e);
      }

  }

  public Todo findOne(String id) {
      Todo todo = null;
      try (final Connection connection = dataSource.getConnection()) {
          this.todoRepository = new TodoRepositoryImpl(connection);
          todo = todoRepository.findOne(id);

      } catch (SQLException e) {
          logger.error("SqlException:", e);
      }
      return todo;
  }
}
