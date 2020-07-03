package com.networknt.example.pdf.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.config.Config;
import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class PdfReportPostHandler implements LightHttpHandler {
    Logger logger = LoggerFactory.getLogger(PdfReportPostHandler.class);
    static ObjectMapper objectMapper = Config.getInstance().getMapper();
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        InputStream result = (InputStream)exchange.getAttachment(BodyHandler.REQUEST_BODY);
        Map<String, Object> jsonMap = SerializationUtils.deserialize(result);
     //   Map<String, Object> jsonMap = objectMapper.readValue(result, Map.class);

        String fileName = (String)jsonMap.get("name");
        byte[] fileBody = (byte[])jsonMap.get("profileFile");
        logger.info("file name:"+ fileName);
        logger.info("file size:"+ fileBody.length);
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(fileName));
    }
}
