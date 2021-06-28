package com.networknt.foodstore.service;

import com.networknt.http.HttpStatus;
import com.networknt.http.MediaType;
import com.networknt.http.RequestEntity;
import com.networknt.http.ResponseEntity;
import com.networknt.http.HttpService;
import com.networknt.foodstore.model.Food;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FoodPostService implements HttpService<Food, String> {
    private static final Logger logger = LoggerFactory.getLogger(FoodPostService.class);

    @Override
    public ResponseEntity invoke(RequestEntity<Food> requestEntity) {
        Food requestBody = requestEntity.getBody();
        logger.debug(requestBody.toString());
        HeaderMap responseHeaders = new HeaderMap();
        responseHeaders.add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = "{}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, responseHeaders, HttpStatus.OK);
        return responseEntity;
    }
}
