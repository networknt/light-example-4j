package com.networknt.bookstore.service;

import com.networknt.http.HttpStatus;
import com.networknt.http.MediaType;
import com.networknt.http.RequestEntity;
import com.networknt.http.ResponseEntity;
import com.networknt.http.HttpService;
import com.networknt.bookstore.model.Book;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BooksPostService implements HttpService<Book, String> {
    private static final Logger logger = LoggerFactory.getLogger(BooksPostService.class);

    @Override
    public ResponseEntity invoke(RequestEntity<Book> requestEntity) {
        Book requestBody = requestEntity.getBody();
        logger.debug(requestBody.toString());
        HeaderMap responseHeaders = new HeaderMap();
        responseHeaders.add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = "{}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, responseHeaders, HttpStatus.OK);
        return responseEntity;
    }
}
