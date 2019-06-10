package com.networknt.market.service;


import com.networknt.client.ClientConfig;
import com.networknt.client.model.HttpVerb;
import com.networknt.client.model.ServiceDef;
import com.networknt.client.orchestration.Http2ServiceRequest;
import com.networknt.client.rest.LightRestClient;
import com.networknt.config.Config;
import com.networknt.exception.ApiException;
import com.networknt.market.model.Market;
import com.networknt.status.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class MarketServiceImpl implements MarketService{
    private static final Logger logger = LoggerFactory.getLogger(MarketServiceImpl.class);
    private static final String PET_STORE = "petstore";
    private static final String FOOD_STORE = "foodstore";
    private static final String BOOK_STORE = "bookstore";
    private static final String COMPUTER_STORE = "computerstore";

    ServiceMapper serviceMapper = (ServiceMapper) Config.getInstance().getJsonObjectConfig(ServiceMapper.CONFIG_NAME, ServiceMapper.class);
    @Override
    public Market getMarket() throws Exception {
        Market market = new Market();
        market.setName("light-4j sample market");
        Http2ServiceRequest listAllPets = getHttp2ServiceRequest(PET_STORE, "/v1/pets", "GET");
        Http2ServiceRequest listAllFood = getHttp2ServiceRequest(FOOD_STORE, "/v1/food", "GET");
        Http2ServiceRequest listAllComputers = getHttp2ServiceRequest(COMPUTER_STORE, "/v1/computers", "GET");
        Http2ServiceRequest listAllBooks = getHttp2ServiceRequest(BOOK_STORE, "/v1/books", "GET");

        CompletableFuture<List> petsFutureResponse = listAllPets.callForTypedObject(List.class);
        CompletableFuture<List> foodFutureResponse = listAllFood.callForTypedObject(List.class);
        CompletableFuture<List> computersFutureResponse = listAllComputers.callForTypedObject(List.class);
        CompletableFuture<List> booksFutureResponse = listAllBooks.callForTypedObject(List.class);

        //We can set value directly as following
/*       market.setPets(petsFutureResponse.get());
          market.setFoodbox(foodFutureResponse.get());
          market.setBooks(booksFutureResponse.get());
          market.setComputers(computersFutureResponse.get()); */

        Collection<CompletableFuture<?>> completableFutures = new HashSet<>();
        completableFutures.add(petsFutureResponse);
        completableFutures.add(foodFutureResponse);
        completableFutures.add(computersFutureResponse);
        completableFutures.add(booksFutureResponse);
        completableFutures.addAll(mapToMarket(market, petsFutureResponse, foodFutureResponse, computersFutureResponse, booksFutureResponse));
        try {
            CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).get(ClientConfig.get().getTimeout(), TimeUnit.MILLISECONDS);
        } catch(Exception e) {
            logger.error("Some information was unavailable when assembling the market value.", e);
            System.out.println("error:" + e);
            throw new ApiException(new Status("ERR10010"));
        }
        return market;
    }


    public  Collection<CompletableFuture<?>> mapToMarket(Market market,CompletableFuture<List> petsFutureResponse, CompletableFuture<List> foodFutureResponse,
                                                              CompletableFuture<List> computersFutureResponse,CompletableFuture<List> booksFutureResponse) {
        Collection<CompletableFuture<?>> completableFutures = new HashSet<>();

        CompletableFuture<Void> setPets = petsFutureResponse.thenAccept(pets -> market.setPets(pets));
        CompletableFuture<Void> setFood = foodFutureResponse.thenAccept(food -> market.setFoodbox(food));
        CompletableFuture<Void> setBooks = booksFutureResponse.thenAccept(books -> market.setBooks(books));
        CompletableFuture<Void> setComputers = computersFutureResponse.thenAccept(computers -> market.setComputers(computers));

        completableFutures.add(setPets);
        completableFutures.add(setFood);
        completableFutures.add(setBooks);
        completableFutures.add(setComputers);
        return completableFutures;
    }

    private Http2ServiceRequest getHttp2ServiceRequest(String serviceName, String path, String method) throws Exception {
        ServiceObject serviceObject = serviceMapper.getServiceMap().get(serviceName);
        ServiceDef serviceDef = new ServiceDef(serviceObject.getProtocol(), serviceObject.getServiceId(), serviceObject.getEnvironment(), serviceObject.getRequestKey());

        if (serviceDef==null) throw new Exception("There is no service config in the service-define.yml file.");
        return  new Http2ServiceRequest(serviceDef, path, HttpVerb.valueOf(method));

    }
}
