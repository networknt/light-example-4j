package com.networknt.petstore.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.body.BodyHandler;
import com.networknt.config.Config;
import com.networknt.exception.ApiException;
import com.networknt.http.*;
import com.networknt.petstore.service.PetStoreService;
import com.networknt.handler.LightHttpHandler;
import com.networknt.service.SingletonServiceFactory;
import com.networknt.status.Status;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import com.networknt.petstore.model.Pet;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class PetsPostHandler implements LightHttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(PetsPostHandler.class);
    private static PetStoreService petStoreService = SingletonServiceFactory.getBean(PetStoreService.class);
    private static final ObjectMapper objectMapper = Config.getInstance().getMapper();
    private  static final String API_ERROR = "ERR30000";

    public PetsPostHandler () {
    }

    //generate method from openAPI  -->     operationId: "createPets"
    protected ResponseEntity createPets(Pet pet) throws ApiException {
        HeaderMap responseHeaders = new HeaderMap();
        responseHeaders.add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        ResponseEntity<String> responseEntity ;
        try {
            petStoreService.addPet(pet);
            responseEntity = new ResponseEntity<>(objectMapper.writeValueAsString(pet), responseHeaders, HttpStatus.CREATED);
            return responseEntity;
        } catch(Exception e) {
            logger.error("Service call error:" + e);
            Status status = new Status(API_ERROR);
            throw new ApiException(status);
        }
    }
    
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Map<String, Object> bodyMap = (Map<String, Object>)exchange.getAttachment(BodyHandler.REQUEST_BODY);
        Pet pet = Config.getInstance().getMapper().convertValue(bodyMap, Pet.class);
        ResponseEntity<String> responseEntity = createPets(pet);
        responseEntity.getHeaders().forEach(values -> {
            exchange.getResponseHeaders().add(values.getHeaderName(), values.getFirst());
        });
        exchange.setStatusCode(responseEntity.getStatusCodeValue());
        exchange.getResponseSender().send(responseEntity.getBody());
    }
}
