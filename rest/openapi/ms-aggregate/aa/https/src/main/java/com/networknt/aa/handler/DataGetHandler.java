
package com.networknt.aa.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.networknt.client.Http2Client;
import com.networknt.cluster.Cluster;
import com.networknt.config.Config;
import com.networknt.exception.ClientException;
import com.networknt.security.JwtHelper;
import com.networknt.service.SingletonServiceFactory;
import io.undertow.UndertowOptions;
import io.undertow.client.ClientConnection;
import io.undertow.client.ClientRequest;
import io.undertow.client.ClientResponse;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.OptionMap;

import java.net.URI;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class DataGetHandler implements HttpHandler {
    static Logger logger = LoggerFactory.getLogger(DataGetHandler.class);
    static Cluster cluster = SingletonServiceFactory.getBean(Cluster.class);
    static String abHost;
    static String acHost;
    static String adHost;
    static String path = "/v1/data";
    static Map<String, Object> securityConfig = (Map) Config.getInstance().getJsonMapConfig(JwtHelper.SECURITY_CONFIG);
    static boolean securityEnabled = (Boolean)securityConfig.get(JwtHelper.ENABLE_VERIFY_JWT);

    static Http2Client client = Http2Client.getInstance();
    static ClientConnection connectionAb;
    static ClientConnection connectionAc;
    static ClientConnection connectionAd;

    public DataGetHandler() {
        try {
            abHost = cluster.serviceToUrl("https", "com.networknt.ab-1.0.0", null, null);
            acHost = cluster.serviceToUrl("https", "com.networknt.ac-1.0.0", null, null);
            adHost = cluster.serviceToUrl("https", "com.networknt.ad-1.0.0", null, null);
            connectionAb = client.connect(new URI(abHost), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true)).get();
            connectionAc = client.connect(new URI(acHost), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true)).get();
            connectionAd = client.connect(new URI(adHost), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true)).get();
        } catch (Exception e) {
            logger.error("Exeption:", e);
        }
    }


    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        List<String> list = new ArrayList<>();
        if(connectionAb == null || !connectionAb.isOpen()) {
            try {
                abHost = cluster.serviceToUrl("https", "com.networknt.ab-1.0.0", null, null);
                connectionAb = client.connect(new URI(abHost), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true)).get();
            } catch (Exception e) {
                logger.error("Exeption:", e);
                throw new ClientException(e);
            }
        }
        if(connectionAc == null || !connectionAc.isOpen()) {
            try {
                acHost = cluster.serviceToUrl("https", "com.networknt.ac-1.0.0", null, null);
                connectionAc = client.connect(new URI(acHost), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true)).get();
            } catch (Exception e) {
                logger.error("Exeption:", e);
                throw new ClientException(e);
            }
        }
        if(connectionAd == null || !connectionAd.isOpen()) {
            try {
                adHost = cluster.serviceToUrl("https", "com.networknt.ad-1.0.0", null, null);
                connectionAd = client.connect(new URI(adHost), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true)).get();
            } catch (Exception e) {
                logger.error("Exeption:", e);
                throw new ClientException(e);
            }
        }
        final CountDownLatch latch = new CountDownLatch(3);
        final AtomicReference<ClientResponse> referenceAb = new AtomicReference<>();
        final AtomicReference<ClientResponse> referenceAc = new AtomicReference<>();
        final AtomicReference<ClientResponse> referenceAd = new AtomicReference<>();
        try {
            ClientRequest requestAb = new ClientRequest().setMethod(Methods.GET).setPath(path);
            if(securityEnabled) client.propagateHeaders(requestAb, exchange);
            connectionAb.sendRequest(requestAb, client.createClientCallback(referenceAb, latch));

            ClientRequest requestAc = new ClientRequest().setMethod(Methods.GET).setPath(path);
            if(securityEnabled) client.propagateHeaders(requestAc, exchange);
            connectionAc.sendRequest(requestAc, client.createClientCallback(referenceAc, latch));

            ClientRequest requestAd = new ClientRequest().setMethod(Methods.GET).setPath(path);
            if(securityEnabled) client.propagateHeaders(requestAd, exchange);
            connectionAd.sendRequest(requestAd, client.createClientCallback(referenceAd, latch));

            latch.await();

            int statusCodeAb = referenceAb.get().getResponseCode();
            if(statusCodeAb >= 300){
                throw new Exception("Failed to call API AB: " + statusCodeAb);
            }
            List<String> abList = Config.getInstance().getMapper().readValue(referenceAb.get().getAttachment(Http2Client.RESPONSE_BODY),
                    new TypeReference<List<String>>(){});
            list.addAll(abList);

            int statusCodeAc = referenceAc.get().getResponseCode();
            if(statusCodeAc >= 300){
                throw new Exception("Failed to call API AC: " + statusCodeAc);
            }
            List<String> acList = Config.getInstance().getMapper().readValue(referenceAc.get().getAttachment(Http2Client.RESPONSE_BODY),
                    new TypeReference<List<String>>(){});
            list.addAll(acList);

            int statusCodeAd = referenceAd.get().getResponseCode();
            if(statusCodeAd >= 300){
                throw new Exception("Failed to call API AD: " + statusCodeAd);
            }
            List<String> adList = Config.getInstance().getMapper().readValue(referenceAd.get().getAttachment(Http2Client.RESPONSE_BODY),
                    new TypeReference<List<String>>(){});
            list.addAll(adList);

        } catch (Exception e) {
            logger.error("Exception:", e);
            throw new ClientException(e);
        }
        list.add("API AA: Message 1");
        list.add("API AA: Message 2");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(list));
    }
}
