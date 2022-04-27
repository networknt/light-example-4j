package com.networknt.account.service;

import com.networknt.http.HttpStatus;
import com.networknt.http.MediaType;
import com.networknt.http.RequestEntity;
import com.networknt.http.ResponseEntity;
import com.networknt.http.HttpService;

import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountsGetService implements HttpService<Void, String> {
    private static final Logger logger = LoggerFactory.getLogger(AccountsGetService.class);

    @Override
    public ResponseEntity invoke(RequestEntity<Void> requestEntity) {
        HeaderMap responseHeaders = new HeaderMap();
        responseHeaders.add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = "[{\"num\":521112392,\"owner\":\"stevehu\",\"type\":\"checking\",\"firstName\":\"Steve\",\"lastName\":\"Hu\",\"sinNumber\":123456789,\"status\":\"open\"},{\"num\":521112321,\"owner\":\"stevehu\",\"type\":\"investment\",\"firstName\":\"Steve\",\"lastName\":\"Hu\",\"sinNumber\":123456789,\"status\":\"open\"},{\"num\":521999128,\"owner\":\"stevehu\",\"type\":\"saving\",\"firstName\":\"Steve\",\"lastName\":\"Hu\",\"sinNumber\":123456789,\"status\":\"closed\"}]";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, responseHeaders, HttpStatus.OK);
        return responseEntity;
    }
}
