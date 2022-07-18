package com.networknt.path;

import com.networknt.rule.IAction;
import com.networknt.rule.RuleActionValue;
import com.networknt.rule.RuleConstants;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * Transform a request path by adding a context header to the prefix. This is normally used on the light-gateway or
 * http-sidecar to change the request from legacy consumer to upgraded services with different paths than original.
 *
 */

public class HeaderPathTransformAction implements IAction {
    private static final Logger logger = LoggerFactory.getLogger(HeaderPathTransformAction.class);

    @Override
    public void performAction(Map<String, Object> objMap, Map<String, Object> resultMap, Collection<RuleActionValue> actionValues) {
        // get the body from the objMap and create a new body in the resultMap. Both in string format.
        resultMap.put(RuleConstants.RESULT, true);
        String requestPath = (String)objMap.get("requestPath");
        String requestURI = (String)objMap.get("requestURI");

        if(logger.isDebugEnabled()) logger.debug("original request path = " + requestPath + " requestURI = " + requestURI);
        HeaderMap headerMap = (HeaderMap)objMap.get("requestHeaders");
        String context = null;
        HeaderValues contextObject = headerMap.get("context");
        if(contextObject != null) context = contextObject.getFirst();
        if(logger.isDebugEnabled()) logger.debug("header context = " + context);
        if(context != null) {
            if(!context.startsWith("/")) context = "/" + context;
            requestPath = context + requestPath;
            requestURI = context + requestURI;
        }
        if(logger.isDebugEnabled()) logger.debug("final requestPath = " + requestPath + " requestURI = " + requestURI);
        resultMap.put("requestPath", requestPath);
        resultMap.put("requestURI", requestURI);
    }
}
