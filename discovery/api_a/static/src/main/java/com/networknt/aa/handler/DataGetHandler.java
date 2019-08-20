package com.networknt.aa.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.networknt.client.Http2Client;
import com.networknt.config.Config;
import com.networknt.exception.ClientException;
import com.networknt.handler.LightHttpHandler;
import com.networknt.security.JwtVerifier;
import io.undertow.UndertowOptions;
import io.undertow.client.ClientConnection;
import io.undertow.client.ClientRequest;
import io.undertow.client.ClientResponse;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.OptionMap;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class DataGetHandler implements LightHttpHandler {
    static String CONFIG_NAME = "api_a";
    static Logger logger = LoggerFactory.getLogger(DataGetHandler.class);
    static String apibHost = (String) Config.getInstance().getJsonMapConfig(CONFIG_NAME).get("api_b_host");
    static String apibPath = (String) Config.getInstance().getJsonMapConfig(CONFIG_NAME).get("api_b_path");
    static String apicHost = (String) Config.getInstance().getJsonMapConfig(CONFIG_NAME).get("api_c_host");
    static String apicPath = (String) Config.getInstance().getJsonMapConfig(CONFIG_NAME).get("api_c_path");
    static Map<String, Object> securityConfig = (Map)Config.getInstance().getJsonMapConfig(JwtVerifier.SECURITY_CONFIG);
    static boolean securityEnabled = (Boolean)securityConfig.get(JwtVerifier.ENABLE_VERIFY_JWT);
    static Http2Client client = Http2Client.getInstance();
    static ClientConnection connectionB;
    static ClientConnection connectionC;

    public DataGetHandler() {
        try {
            connectionB = client.connect(new URI(apibHost), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true)).get();
            connectionC = client.connect(new URI(apicHost), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true)).get();
        } catch (Exception e) {
            logger.error("Exeption:", e);
        }
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        List<String> list = new ArrayList<>();
        if(connectionB == null || !connectionB.isOpen()) {
            try {
                connectionB = client.connect(new URI(apibHost), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true)).get();
            } catch (Exception e) {
                logger.error("Exeption:", e);
                throw new ClientException(e);
            }
        }
        if(connectionC == null || !connectionC.isOpen()) {
            try {
                connectionC = client.connect(new URI(apicHost), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true)).get();
            } catch (Exception e) {
                logger.error("Exeption:", e);
                throw new ClientException(e);
            }
        }
        final CountDownLatch latch = new CountDownLatch(2);
        final AtomicReference<ClientResponse> referenceB = new AtomicReference<>();
        final AtomicReference<ClientResponse> referenceC = new AtomicReference<>();
        try {
            ClientRequest requestB = new ClientRequest().setMethod(Methods.GET).setPath(apibPath);
            if(securityEnabled) client.propagateHeaders(requestB, exchange);
            connectionB.sendRequest(requestB, client.createClientCallback(referenceB, latch));

            ClientRequest requestC = new ClientRequest().setMethod(Methods.GET).setPath(apicPath);
            if(securityEnabled) client.propagateHeaders(requestC, exchange);
            connectionC.sendRequest(requestB, client.createClientCallback(referenceC, latch));

            latch.await();

            int statusCodeB = referenceB.get().getResponseCode();
            if(statusCodeB >= 300){
                throw new Exception("Failed to call API B: " + statusCodeB);
            }
            List<String> apibList = Config.getInstance().getMapper().readValue(referenceB.get().getAttachment(Http2Client.RESPONSE_BODY),
                    new TypeReference<List<String>>(){});
            list.addAll(apibList);

            int statusCodeC = referenceC.get().getResponseCode();
            if(statusCodeC >= 300){
                throw new Exception("Failed to call API C: " + statusCodeC);
            }
            List<String> apicList = Config.getInstance().getMapper().readValue(referenceC.get().getAttachment(Http2Client.RESPONSE_BODY),
                    new TypeReference<List<String>>(){});
            list.addAll(apicList);
        } catch (Exception e) {
            logger.error("Exception:", e);
            throw new ClientException(e);
        }
        list.add("API A: Message 1");
        list.add("API A: Message 2");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(list));
    }
}
