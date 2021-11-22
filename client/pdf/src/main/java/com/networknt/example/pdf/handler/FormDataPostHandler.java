package com.networknt.example.pdf.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.config.Config;
import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.util.HttpString;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public class FormDataPostHandler implements LightHttpHandler {
    Logger logger = LoggerFactory.getLogger(FormDataPostHandler.class);
    static ObjectMapper objectMapper = Config.getInstance().getMapper();
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        Map<String, Object> jsonMap = (Map<String, Object>) exchange.getAttachment(com.networknt.body.BodyHandler.REQUEST_BODY);

        String comment = (String)jsonMap.get("comments");
        FormData.FileItem fileItem = (FormData.FileItem)jsonMap.get("file");

        File targetFile = new File("src/main/resources/targetFile.pdf");
        InputStream inputStream = fileItem.getInputStream();
        java.nio.file.Files.copy(
                inputStream,
                targetFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        IOUtils.closeQuietly(inputStream);

     //   logger.info("file name:"+ fileName);
        logger.info("file size:"+ fileItem.getFileSize());
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
      //  exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(fileName));
          exchange.getResponseSender().send(comment + "; \n file size:" + fileItem.getFileSize());
    }
}
