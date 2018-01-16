
package com.networknt.saga.example.order.service.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.body.BodyHandler;
import com.networknt.config.Config;
import com.networknt.example.sagas.ordersandcustomers.commondomain.Money;
import com.networknt.example.sagas.ordersandcustomers.order.common.OrderDetails;
import com.networknt.example.sagas.ordersandcustomers.order.domain.Order;
import com.networknt.example.sagas.ordersandcustomers.order.service.OrderService;
import com.networknt.example.sagas.ordersandcustomers.order.web.CreateOrderRequest;
import com.networknt.example.sagas.ordersandcustomers.order.web.CreateOrderResponse;
import com.networknt.service.SingletonServiceFactory;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.Map;

public class OrdersPostHandler implements HttpHandler {

    private OrderService orderService = SingletonServiceFactory.getBean(OrderService.class);
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        // add a new object
        Map s = (Map)exchange.getAttachment(BodyHandler.REQUEST_BODY);
        String json = mapper.writeValueAsString(s);
        CreateOrderRequest createOrderRequest = mapper.readValue(json, CreateOrderRequest.class);

        Order order = orderService.createOrder(new OrderDetails(createOrderRequest.getCustomerId(), new Money(createOrderRequest.getOrderTotal())));

        CreateOrderResponse createOrderResponse = new CreateOrderResponse(order.getId());

        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(createOrderResponse));



    }
}
