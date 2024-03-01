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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * Transform a response body from XML to JSON in the response so that a REST client can access a backend SOAP
 * service.
 *
 */

public class Xml2JsonResponseTransformAction implements IAction {
    private static final Logger logger = LoggerFactory.getLogger(Xml2JsonResponseTransformAction.class);
    ObjectMapper objectMapper = new ObjectMapper();
    XmlMapper xmlMapper = new XmlMapper();

    @Override
    public void performAction(Map<String, Object> objMap, Map<String, Object> resultMap, Collection<RuleActionValue> actionValues) {
        // get the response body from the objMap and create a new response body in the resultMap. Both in string format.
        resultMap.put(RuleConstants.RESULT, true);
        String responseBody = (String)objMap.get("responseBody");
        if(logger.isTraceEnabled()) logger.trace("original response body = " + responseBody);
        // transform the body from json to xml.
        resultMap.put("responseBody", transform(responseBody));
        // transform the content type header.
        HeaderMap headerMap = (HeaderMap)objMap.get("responseHeaders");
        String contentType = null;
        HeaderValues contentTypeObject = headerMap.get(Headers.CONTENT_TYPE);
        if(contentTypeObject != null) contentType = contentTypeObject.getFirst();
        if(logger.isTraceEnabled()) logger.trace("header contentType = " + contentType);
        if(contentType != null && contentType.startsWith("text/xml")) {
            // change it to text/xml
            headerMap.remove(Headers.CONTENT_TYPE);
            headerMap.put(Headers.CONTENT_TYPE, "application/json");
            if(logger.isTraceEnabled()) logger.trace("response contentType has been changed from tex/xml to tapplication/json");
        }
    }

    public String transform(String input) {
        String output = null;
        try {
            Flower poppy = xmlMapper.readValue(input, Flower.class);
            output = objectMapper.writeValueAsString(poppy);
            if(logger.isTraceEnabled()) logger.trace("transformed response body = " + output);
        } catch (JsonProcessingException e) {
            logger.error("Transform exception:", e);
        }
        return output;
    }

}
