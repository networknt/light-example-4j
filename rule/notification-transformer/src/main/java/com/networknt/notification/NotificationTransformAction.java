package com.networknt.notification;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.rule.IAction;
import com.networknt.rule.RuleActionValue;
import com.networknt.rule.RuleConstants;
import io.undertow.util.StatusCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;

/**
 * Transform a structured JSON notification to something understand by the legacy consumer. Basically, this is an example
 * of JSON to JSON transformation with some calculations when moving the data elements.
 *
 */
public class NotificationTransformAction implements IAction {
    private static final Logger logger = LoggerFactory.getLogger(NotificationTransformAction.class);
    static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void performAction(Map<String, Object> objMap, Map<String, Object> resultMap, Collection<RuleActionValue> actionValues) {
        // get the body from the objMap and create a new body in the resultMap. Both in string format.
        resultMap.put(RuleConstants.RESULT, true);
        String responseBody = (String)objMap.get("responseBody");
        if(logger.isDebugEnabled()) logger.debug("original response body = " + responseBody);
        String statusCode = StatusCodes.getReason((Integer)objMap.get("statusCode")).toUpperCase();
        if(logger.isDebugEnabled()) logger.debug("statusCode = " + statusCode);
        String modifiedBody = transform(responseBody, statusCode);
        resultMap.put("responseBody", modifiedBody);
    }

    private String transform(String responseBody, String statusCode) {
        // the source input is a JSON standard notification response. Convert to an POJO.
        String output = null;
        try {
            Map<String, Object> targetMap = new LinkedHashMap<>();
            Map<String, Object> sourceMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
            targetMap.put("status", statusCode);
            // get the first notification from the list.
            Map<String, Object> notification = null;
            List notifications = (List) sourceMap.get("notifications");
            if (notifications != null && notifications.size() > 0) {
                notification = (Map<String, Object>) notifications.get(0);
            }
            if (notification != null) {
                targetMap.put("message", notification.get("message"));
                Map<String, Object> timestamp = new LinkedHashMap<>();
                long ts = (Long) notification.get("timestamp");
                Instant instant = Instant.ofEpochMilli(ts);
                timestamp.put("epochSecond", instant.getEpochSecond());
                timestamp.put("nano", instant.getNano());
                targetMap.put("timestamp", timestamp);
            }
            output = objectMapper.writeValueAsString(targetMap);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return output;
    }
}
