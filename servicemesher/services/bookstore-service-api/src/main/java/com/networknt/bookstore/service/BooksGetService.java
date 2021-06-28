package com.networknt.bookstore.service;

import com.networknt.http.HttpStatus;
import com.networknt.http.MediaType;
import com.networknt.http.RequestEntity;
import com.networknt.http.ResponseEntity;
import com.networknt.http.HttpService;

import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BooksGetService implements HttpService<Void, String> {
    private static final Logger logger = LoggerFactory.getLogger(BooksGetService.class);

    @Override
    public ResponseEntity invoke(RequestEntity<Void> requestEntity) {
        HeaderMap responseHeaders = new HeaderMap();
        responseHeaders.add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = "[{\"id\":1,\"name\":\"Educated\",\"author\":\"James Bond\"},{\"id\":2,\"name\":\"The Amazon Job\",\"author\":\"Randy Grieser\"}]";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, responseHeaders, HttpStatus.OK);
        return responseEntity;
    }
}
