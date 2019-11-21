package com.networknt.example.pdf.handler;

import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.io.ByteArrayOutputStream;
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
            RandomAccessFile reader = new RandomAccessFile("src/main/resources/sample.pdf", "r");
            FileChannel ch = reader.getChannel();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int bufferSize = 1024;
            if (bufferSize > ch.size()) {
                bufferSize = (int) ch.size();
            }

            ByteBuffer buff = ByteBuffer.allocate(bufferSize);

            while(ch.read(buff) > 0) {
                out.write(buff.array(), 0, buff.position());
                buff.clear();
            }

            response = out.toByteArray();
        } catch (Exception e) {

        }

        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/octet-stream");
        exchange.getResponseSender().send(ByteBuffer.wrap(response));
    }
}
