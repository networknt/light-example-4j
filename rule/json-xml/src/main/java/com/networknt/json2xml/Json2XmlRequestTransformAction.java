package com.networknt.json2xml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.networknt.rule.IAction;
import com.networknt.rule.RuleActionValue;
import com.networknt.rule.RuleConstants;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Transform a request body from the JSON to XML in order to access SOAP API from rest client. This can be used in
 * light-gateway or http-sidecar to change the request from newly built consumer to access legacy service.
 *
 */

public class Json2XmlRequestTransformAction implements IAction {
    private static final Logger logger = LoggerFactory.getLogger(Json2XmlRequestTransformAction.class);
    ObjectMapper objectMapper = new ObjectMapper();
    XmlMapper xmlMapper = new XmlMapper();

    @Override
    public void performAction(Map<String, Object> objMap, Map<String, Object> resultMap, Collection<RuleActionValue> actionValues) {
        // get the request body from the objMap and create a new request body in the resultMap. Both in string format.
        resultMap.put(RuleConstants.RESULT, true);
        String requestBody = (String)objMap.get("requestBody");
        if(logger.isTraceEnabled()) logger.trace("original request body = " + requestBody);
        // transform the body from json to xml.
        resultMap.put("requestBody", transform(requestBody));
        // transform the content type header.
        HeaderMap headerMap = (HeaderMap)objMap.get("requestHeaders");
        String contentType = null;
        HeaderValues contentTypeObject = headerMap.get(Headers.CONTENT_TYPE);
        if(contentTypeObject != null) contentType = contentTypeObject.getFirst();
        if(logger.isTraceEnabled()) logger.trace("header contentType = " + contentType);
        if(contentType != null && contentType.startsWith("application/json")) {
            // change it to text/xml
            headerMap.remove(Headers.CONTENT_TYPE);
            headerMap.put(Headers.CONTENT_TYPE, "text/xml");
            if(logger.isTraceEnabled()) logger.trace("request contentType has been changed from application/json to text/xml");
        }
    }

    public String transform(String input) {
        String output = null;
        try {
            Flower poppy = objectMapper.readValue(input, Flower.class);
            // this is direct transformation; however, you can convert the JSON to an intermediate object and then to
            // another object with different structure and calculated data elements.
            output = xmlMapper.writeValueAsString(poppy);
            if(logger.isTraceEnabled()) logger.trace("transformed request body = " + output);
        } catch (JsonProcessingException e) {
            logger.error("Transform exception:", e);
        }
        return output;
    }

}
