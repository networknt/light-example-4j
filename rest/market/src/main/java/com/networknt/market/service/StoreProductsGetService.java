package com.networknt.market.service;

import com.networknt.client.http.Http2ServiceRequest;
import com.networknt.config.Config;
import com.networknt.config.JsonMapper;
import com.networknt.http.HttpStatus;
import com.networknt.http.MediaType;
import com.networknt.http.RequestEntity;
import com.networknt.http.ResponseEntity;
import com.networknt.http.HttpService;

import com.networknt.market.MarketConfig;
import com.networknt.market.ServiceRef;
import com.networknt.market.model.Product;
import com.networknt.monad.Failure;
import com.networknt.monad.Result;
import com.networknt.monad.Success;
import com.networknt.status.Status;
import com.networknt.utility.Constants;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

public class StoreProductsGetService implements HttpService<Void, String> {
    private static final Logger logger = LoggerFactory.getLogger(StoreProductsGetService.class);
    public static final String referenceAPI = "referenceAPI";
    public static final String GENERIC_EXCEPTION = "ERR10014";

    MarketConfig config = (MarketConfig) Config.getInstance().getJsonObjectConfig(MarketConfig.CONFIG_NAME, MarketConfig.class);

    @Override
    public ResponseEntity invoke(RequestEntity<Void> requestEntity) {
        HeaderMap responseHeaders = new HeaderMap();
        responseHeaders.add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        ResponseEntity<String> responseEntity;
        try {
            Http2ServiceRequest getStoreData = getHttp2ServiceRequest(referenceAPI);
            List list = getStoreData.callForTypedObject(List.class).get();
            responseEntity = new ResponseEntity<>(JsonMapper.toJson(list), responseHeaders, HttpStatus.resolve(200));
        } catch (Exception e) {
            logger.error("API call error: ", e);
            Status status = new Status(GENERIC_EXCEPTION, e.getMessage());
            responseEntity = new ResponseEntity<>(status.toString(), responseHeaders, HttpStatus.resolve(status.getStatusCode()));
        }
        return responseEntity;
    }

    private ServiceRef getService(String name) {
        return config.getApiServiceRef().get(name);
    }

    private Http2ServiceRequest getHttp2ServiceRequest(String serviceName) throws Exception {
        ServiceRef serviceRef = getService(serviceName);
        if(serviceRef == null) throw new Exception("Missing service config in market.yml");
        String proxyUrl = config.getProxyUrl();
        if(logger.isDebugEnabled()) logger.debug("proxyUrl = " + proxyUrl);
        Http2ServiceRequest http2ServiceRequest = new Http2ServiceRequest(new URI(proxyUrl), serviceRef.getPath(), new HttpString(serviceRef.getMethod()));
        http2ServiceRequest.addRequestHeader("Host", "localhost");
        http2ServiceRequest.addRequestHeader(Constants.TRACEABILITY_ID_STRING, "traceabilityId");
        if(serviceRef.getServiceUrl() != null) {
            http2ServiceRequest.addRequestHeader(Constants.SERVICE_URL_STRING, serviceRef.getServiceUrl());
        } else {
            http2ServiceRequest.addRequestHeader(Constants.SERVICE_ID_STRING, serviceRef.getServiceId());
        }
        return http2ServiceRequest;
    }
}
