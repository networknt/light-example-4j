
package com.networknt.saga.example.order.service.handler;

import com.networknt.config.Config;
import com.networknt.example.sagas.ordersandcustomers.order.domain.Order;
import com.networknt.example.sagas.ordersandcustomers.order.service.OrderService;
import com.networknt.example.sagas.ordersandcustomers.order.web.GetOrderResponse;
import com.networknt.service.SingletonServiceFactory;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersOrderIdGetHandler implements HttpHandler {
    private OrderService orderService = SingletonServiceFactory.getBean(OrderService.class);
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        String orderId = exchange.getQueryParameters().get("orderId").getFirst();
        System.out.println("value->: " + orderId);

        Order order = orderService.getOrderById(Long.valueOf(orderId));
        if (order == null ) {

            exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
            exchange.getResponseSender().send("No order with input order Id");
        } else {
            GetOrderResponse getOrderResponse = new GetOrderResponse(order.getId(), order.getState());
            exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
            exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(getOrderResponse));
        }



        
    }
}
