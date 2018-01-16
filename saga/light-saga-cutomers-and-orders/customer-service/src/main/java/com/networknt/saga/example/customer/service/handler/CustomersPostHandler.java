
package com.networknt.saga.example.customer.service.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.body.BodyHandler;
import com.networknt.config.Config;
import com.networknt.example.sagas.ordersandcustomers.customer.domain.Customer;
import com.networknt.example.sagas.ordersandcustomers.customer.service.CustomerService;
import com.networknt.example.sagas.ordersandcustomers.customer.web.CreateCustomerRequest;
import com.networknt.service.SingletonServiceFactory;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.Map;

public class CustomersPostHandler implements HttpHandler {

    private CustomerService customerService = SingletonServiceFactory.getBean(CustomerService.class);

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        // add a new object
        Map s = (Map)exchange.getAttachment(BodyHandler.REQUEST_BODY);
        String json = mapper.writeValueAsString(s);
        CreateCustomerRequest createCustomerRequest = mapper.readValue(json, CreateCustomerRequest.class);
        Customer customer = customerService.createCustomer(createCustomerRequest.getName(), createCustomerRequest.getCreditLimitMoney());


        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(customer));


    }
}
