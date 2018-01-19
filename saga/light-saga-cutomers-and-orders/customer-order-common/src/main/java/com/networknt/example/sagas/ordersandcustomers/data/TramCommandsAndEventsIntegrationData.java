package com.networknt.example.sagas.ordersandcustomers.data;

public class TramCommandsAndEventsIntegrationData {

  private long now = System.currentTimeMillis();
  private String commandDispatcherId = "command-dispatcher-0000001";
  private String commandChannel = "command-channel-0000001";
  private String aggregateDestination = "aggregate-destination-0000001";
  private String eventDispatcherId  = "event-dispatcher-0000001";

  public String getAggregateDestination() {
    return aggregateDestination;
  }


  public String getCommandDispatcherId() {
    return commandDispatcherId;
  }

  public String getCommandChannel() {
    return commandChannel;
  }

  public String getEventDispatcherId() {
    return eventDispatcherId;
  }
}
