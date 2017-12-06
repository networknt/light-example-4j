package com.networknt.eventuate.todolist;


import com.networknt.eventuate.common.DispatchedEvent;
import com.networknt.eventuate.common.EventHandlerMethod;
import com.networknt.eventuate.common.EventSubscriber;
import com.networknt.eventuate.todolist.common.event.TodoCreatedEvent;
import com.networknt.eventuate.todolist.common.event.TodoDeletedEvent;
import com.networknt.eventuate.todolist.common.event.TodoUpdatedEvent;
import com.networknt.eventuate.todolist.common.model.TodoInfo;
import com.networknt.service.SingletonServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@EventSubscriber(id = "todoQuerySideEventHandlers")
public class TodoQueryWorkflow {
    static final private Logger logger = LoggerFactory.getLogger(TodoQueryWorkflow.class);

    private TodoQueryService service =
            (TodoQueryService)SingletonServiceFactory.getBean(TodoQueryService.class);

    public TodoQueryWorkflow() {
    }

    @EventHandlerMethod
    public void create(DispatchedEvent<TodoCreatedEvent> de) {
        logger.debug("TodoCreateEvent received " + de);
        TodoInfo todo = de.getEvent().getTodo();
        service.save(de.getEntityId(), todo);
    }

    @EventHandlerMethod
    public void delete(DispatchedEvent<TodoDeletedEvent> de) {
        logger.debug("TodoDeletedEvent received " + de);
        service.remove(de.getEntityId());
    }

    @EventHandlerMethod
    public void update(DispatchedEvent<TodoUpdatedEvent> de) {
        logger.debug("TodoUpdatedEvent received " + de);
        TodoInfo todo = de.getEvent().getTodo();
        service.save(de.getEntityId(), todo);
    }
}
