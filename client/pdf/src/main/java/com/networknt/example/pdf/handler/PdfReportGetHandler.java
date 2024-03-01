package com.networknt.example.pdf.handler;

import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PdfReportGetHandler implements LightHttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        byte[] response = null;

        try {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("sample.pdf");
            response = IOUtils.toByteArray(resourceAsStream);
        } catch (Exception e) {

        }

        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/octet-stream");
        exchange.getResponseSender().send(ByteBuffer.wrap(response));
    }
}
