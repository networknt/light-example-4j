package com.networknt.petstore.service;

import com.networknt.http.HttpStatus;
import com.networknt.http.MediaType;
import com.networknt.http.RequestEntity;
import com.networknt.http.ResponseEntity;
import com.networknt.http.HttpService;

import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationsGetService implements HttpService<Void, String> {
    private static final Logger logger = LoggerFactory.getLogger(NotificationsGetService.class);

    @Override
    public ResponseEntity invoke(RequestEntity<Void> requestEntity) {
        HeaderMap responseHeaders = new HeaderMap();
        responseHeaders.add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = "{\"data\":null,\"notifications\":[{\"code\":\"ERR00610000\",\"message\":\"Exception in getting service:Unable to create user info\",\"timestamp\":1655739885937,\"metadata\":null,\"description\":\"Internal Server Error\"}]}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, responseHeaders, HttpStatus.OK);
        return responseEntity;
    }
}
