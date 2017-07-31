package com.networknt.todolist.restquery;

import com.networknt.eventuate.common.EventContext;
import com.networknt.eventuate.common.SubscriberOptions;
import com.networknt.eventuate.common.impl.EventIdTypeAndData;
import com.networknt.eventuate.common.impl.SerializedEvent;
import com.networknt.eventuate.common.impl.sync.AggregateCrud;
import com.networknt.eventuate.common.impl.sync.AggregateEvents;
import com.networknt.eventuate.jdbc.AbstractEventuateJdbcAggregateStore;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public class EventuateLocalTestAggregateStore extends AbstractEventuateJdbcAggregateStore
        implements AggregateCrud, AggregateEvents {

  private AtomicLong eventOffset = new AtomicLong();
  private final Map<String, List<Subscription>> aggregateTypeToSubscription = new HashMap<>();

  public EventuateLocalTestAggregateStore(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  @SuppressWarnings("ReturnValueIgnored")
  protected void publish(String aggregateType, String aggregateId, List<EventIdTypeAndData> eventsWithIds) {
    System.out.println("Handler:" +  aggregateTypeToSubscription.size());
    synchronized (aggregateTypeToSubscription) {
      List<Subscription> subscriptions = aggregateTypeToSubscription.get(aggregateType);
      if (subscriptions != null)
        for (Subscription subscription : subscriptions) {
          for (EventIdTypeAndData event : eventsWithIds) {
            if (subscription.isInterestedIn(aggregateType, event.getEventType()))
              subscription.handler.apply(new SerializedEvent(event.getId(), aggregateId, aggregateType, event.getEventData(), event.getEventType(),
                      aggregateId.hashCode() % 8,
                      eventOffset.getAndIncrement(),
                      new EventContext(event.getId().asString()), event.getMetadata()));
          }
        }
    }

  }


  static class Subscription {

    private final String subscriberId;
    private final Map<String, Set<String>> aggregatesAndEvents;
    private final Function<SerializedEvent, CompletableFuture<?>> handler;

    public Subscription(String subscriberId, Map<String, Set<String>> aggregatesAndEvents, Function<SerializedEvent, CompletableFuture<?>> handler) {

      this.subscriberId = subscriberId;
      this.aggregatesAndEvents = aggregatesAndEvents;
      this.handler = handler;
    }

    public boolean isInterestedIn(String aggregateType, String eventType) {
      return aggregatesAndEvents.get(aggregateType) != null && aggregatesAndEvents.get(aggregateType).contains(eventType);
    }
  }

  @Override
  public void subscribe(String subscriberId, Map<String, Set<String>> aggregatesAndEvents, SubscriberOptions options, Function<SerializedEvent, CompletableFuture<?>> handler) {
    // TODO handle options
    Subscription subscription = new Subscription(subscriberId, aggregatesAndEvents, handler);
    synchronized (aggregateTypeToSubscription) {
      for (String aggregateType : aggregatesAndEvents.keySet()) {
        List<Subscription> existing = aggregateTypeToSubscription.get(aggregateType);
        if (existing == null) {
          existing = new LinkedList<>();
          aggregateTypeToSubscription.put(aggregateType, existing);
          System.out.println("add Handler:" +  existing.size());

        }
        existing.add(subscription);
      }
    }
  }


}
