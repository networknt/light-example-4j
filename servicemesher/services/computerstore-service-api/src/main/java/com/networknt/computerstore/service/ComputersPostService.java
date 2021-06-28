package com.networknt.computerstore.service;

import com.networknt.http.HttpStatus;
import com.networknt.http.MediaType;
import com.networknt.http.RequestEntity;
import com.networknt.http.ResponseEntity;
import com.networknt.http.HttpService;
import com.networknt.computerstore.model.Computer;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComputersPostService implements HttpService<Computer, String> {
    private static final Logger logger = LoggerFactory.getLogger(ComputersPostService.class);

    @Override
    public ResponseEntity invoke(RequestEntity<Computer> requestEntity) {
        Computer requestBody = requestEntity.getBody();
        logger.debug(requestBody.toString());
        HeaderMap responseHeaders = new HeaderMap();
        responseHeaders.add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = "{}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, responseHeaders, HttpStatus.OK);
        return responseEntity;
    }
}
