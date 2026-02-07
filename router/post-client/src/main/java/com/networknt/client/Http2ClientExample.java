package com.networknt.client;

import com.networknt.client.oauth.TokenResponse;
import com.networknt.client.simplepool.SimpleConnectionHolder;
import com.networknt.cluster.Cluster;
import com.networknt.config.Config;
import com.networknt.exception.ClientException;
import com.networknt.service.SingletonServiceFactory;
import io.undertow.UndertowOptions;
import io.undertow.client.ClientConnection;
import io.undertow.client.ClientRequest;
import io.undertow.client.ClientResponse;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.IoUtils;
import org.xnio.OptionMap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.networknt.client.oauth.OauthHelper.encodeCredentials;

/**
 * This is a stand along java client application that is used to demo how to
 * communicate with services built on top of light-*-4j framework through light
 * client model, specifically Http2Client
 *
 * @author Steve Hu
 */
public class Http2ClientExample {
    static final Logger logger = LoggerFactory.getLogger(Http2ClientExample.class);
    // This should be coming from a config file for your app and set it true if OAuth 2.0
    // provider services are available and configured in client.yml and secret.yml
    static final boolean securityEnabled = false;
    // Get the singleton Cluster instance
    static Cluster cluster = SingletonServiceFactory.getBean(Cluster.class);
    // Get the singleton Http2Client instance
    static Http2Client client = Http2Client.getInstance();
    // This is a connection that is shared by multiple requests and won't close until the app exits.
    static ClientConnection reusedConnection;
    static String apiHost;
    long expiration;

    public static void main(String[] args) throws Exception {
        Http2ClientExample e = new Http2ClientExample();
        //e.testWrongPath();
        //e.testDirect4k();
        //e.testDirect48k();
        //e.testDirect1000k();
        //e.testRouterHttps4k();
        //e.testRouterHttps48k();
        e.testRouterHttps1000k();
        //e.testProxy4k();
        //e.testProxy48k();
        System.exit(0);
    }

    /**
     * This is direct call to the post-service with a 4k json as body
     * on port 8443 which the post-service is listening.
     *
     * @throws Exception
     */
    public void testWrongPath() throws Exception {
        ClientConnection connection = null;
        try {
            SimpleConnectionHolder.ConnectionToken token = client.borrow(new URI("https://localhost:8443"), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true));

            connection = (ClientConnection) token.getRawConnection();
            final String json = getResourceFileAsString("4k.json");
            if(logger.isDebugEnabled()) logger.debug(json);
            final CountDownLatch latch = new CountDownLatch(1);
            final AtomicReference<ClientResponse> reference = new AtomicReference<>();
            final ClientRequest request = new ClientRequest().setMethod(Methods.POST).setPath("/v1/post");
            request.getRequestHeaders().put(Headers.HOST, "localhost");
            request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
            request.getRequestHeaders().put(Headers.CONTENT_TYPE, "application/json");
            connection.sendRequest(request, client.createClientCallback(reference, latch, json));
            latch.await(100, TimeUnit.MILLISECONDS);
            int statusCode = reference.get().getResponseCode();
            String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
            if(statusCode != 200) System.out.println("testDirect4k failed with statusCode = " + statusCode + " body = " + body);
            System.out.println(body);
        } finally {
            client.restore(token);
        }
    }

    /**
     * This is direct call to the post-service with a 4k json as body
     * on 8443 which the post-service is listening
     *
     * @throws Exception
     */
    public void testDirect4k() throws Exception {
        ClientConnection connection = null;
        try {
            SimpleConnectionHolder.ConnectionToken token = client.borrow(new URI("https://localhost:8443"), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true));

            connection = (ClientConnection) token.getRawConnection();
            final String json = getResourceFileAsString("4k.json");
            if(logger.isDebugEnabled()) logger.debug(json);
            for(int i = 0; i < 100; i++) {
                final CountDownLatch latch = new CountDownLatch(1);
                final AtomicReference<ClientResponse> reference = new AtomicReference<>();
                final ClientRequest request = new ClientRequest().setMethod(Methods.POST).setPath("/v1/postData");
                request.getRequestHeaders().put(Headers.HOST, "localhost");
                request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
                request.getRequestHeaders().put(Headers.CONTENT_TYPE, "application/json");
                connection.sendRequest(request, client.createClientCallback(reference, latch, json));
                latch.await(100, TimeUnit.MILLISECONDS);
                int statusCode = reference.get().getResponseCode();
                String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
                if(statusCode != 200) System.out.println("testDirect4k failed with statusCode = " + statusCode + " body = " + body);
                System.out.println(body);
            }
        } finally {
            client.restore(token);
        }

    }

    /**
     * This is direct call to the post-service with a 48k json as body
     * on 8443 which the post-service is listening.
     *
     * @throws Exception
     */
    public void testDirect48k() throws Exception {
        ClientConnection connection = null;
        try {
            SimpleConnectionHolder.ConnectionToken token = client.borrow(new URI("https://localhost:8443"), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true));

            connection = (ClientConnection) token.getRawConnection();
            final String json = getResourceFileAsString("48k.json");
            if(logger.isDebugEnabled()) logger.debug(json);
            for(int i = 0; i < 100; i++) {
                final CountDownLatch latch = new CountDownLatch(1);
                final AtomicReference<ClientResponse> reference = new AtomicReference<>();
                final ClientRequest request = new ClientRequest().setMethod(Methods.POST).setPath("/v1/postData");
                request.getRequestHeaders().put(Headers.HOST, "localhost");
                request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
                request.getRequestHeaders().put(Headers.CONTENT_TYPE, "application/json");
                connection.sendRequest(request, client.createClientCallback(reference, latch, json));
                latch.await(100, TimeUnit.MILLISECONDS);
                int statusCode = reference.get().getResponseCode();
                String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
                if(statusCode != 200) System.out.println("testDirect4k failed with statusCode = " + statusCode + " body = " + body);
                if(!body.startsWith("[")) System.out.println(body);
            }
        } finally {
            client.restore(token);
        }
    }

    /**
     * This is direct call to the post-service with a 1000k json as body
     * on 8443 which the post-service is listening.
     *
     * @throws Exception
     */
    public void testDirect1000k() throws Exception {
        ClientConnection connection = null;
        try {
            SimpleConnectionHolder.ConnectionToken token = client.borrow(new URI("https://localhost:8443"), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true));

            connection = (ClientConnection) token.getRawConnection();
            final String json = getResourceFileAsString("1000k.json");
            if(logger.isDebugEnabled()) logger.debug(json);
            for(int i = 0; i < 100; i++) {
                final CountDownLatch latch = new CountDownLatch(1);
                final AtomicReference<ClientResponse> reference = new AtomicReference<>();
                final ClientRequest request = new ClientRequest().setMethod(Methods.POST).setPath("/v1/postData");
                request.getRequestHeaders().put(Headers.HOST, "localhost");
                request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
                request.getRequestHeaders().put(Headers.CONTENT_TYPE, "application/json");
                connection.sendRequest(request, client.createClientCallback(reference, latch, json));
                latch.await();
                int statusCode = reference.get().getResponseCode();
                String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
                if(statusCode != 200) System.out.println("testDirect4k failed with statusCode = " + statusCode + " body = " + body);
                if(!body.startsWith("[")) System.out.println(body);
            }
        } finally {
            client.restore(token);
        }
    }

    /**
     * This is direct call to the post-service with a 4k json as body
     * on 8080 which the post-router is listening
     *
     * @throws Exception
     */
    public void testRouterHttps4k() throws Exception {
        ClientConnection connection = null;
        try {
            SimpleConnectionHolder.ConnectionToken token = client.borrow(new URI("https://localhost:8000"), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true));

            connection = (ClientConnection) token.getRawConnection();
            final String json = getResourceFileAsString("4k.json");
            for(int i = 0; i < 100; i++) {
                final CountDownLatch latch = new CountDownLatch(1);
                final AtomicReference<ClientResponse> reference = new AtomicReference<>();
                final ClientRequest request = new ClientRequest().setMethod(Methods.POST).setPath("/v1/postData");
                request.getRequestHeaders().put(Headers.HOST, "localhost");
                request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
                request.getRequestHeaders().put(Headers.CONTENT_TYPE, "application/json");
                connection.sendRequest(request, client.createClientCallback(reference, latch, json));
                latch.await();
                int statusCode = reference.get().getResponseCode();
                String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
                if(statusCode != 200) System.out.println("testRoter4k failed with statusCode = " + statusCode + " body = " + body);
                System.out.println(body);
            }
        } finally {
            client.restore(token);
        }

    }

    /**
     * This is direct call to the post-service with a 48k json as body
     * on 8080 which the post-router is listening.
     *
     * @throws Exception
     */
    public void testRouterHttps48k() throws Exception {
        ClientConnection connection = null;
        try {
            SimpleConnectionHolder.ConnectionToken token = client.borrow(new URI("https://localhost:8000"), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true));

            connection = (ClientConnection) token.getRawConnection();
            final String json = getResourceFileAsString("48k.json");
            for(int i = 0; i < 100; i++) {
                final CountDownLatch latch = new CountDownLatch(1);
                final AtomicReference<ClientResponse> reference = new AtomicReference<>();
                final ClientRequest request = new ClientRequest().setMethod(Methods.POST).setPath("/v1/postData");
                request.getRequestHeaders().put(Headers.HOST, "localhost");
                request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
                request.getRequestHeaders().put(Headers.CONTENT_TYPE, "application/json");
                connection.sendRequest(request, client.createClientCallback(reference, latch, json));
                latch.await(1000, TimeUnit.MILLISECONDS);
                int statusCode = reference.get().getResponseCode();
                String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
                if(statusCode != 200) System.out.println("testRouter4k failed with statusCode = " + statusCode + " body = " + body);
                System.out.println(i);
                if(!body.startsWith("[")) System.out.println(body);
            }
        } finally {
            client.restore(token);
        }
    }


    /**
     * This is direct call to the post-service with a 48k json as body
     * on 8080 which the post-router is listening.
     *
     * @throws Exception
     */
    public void testRouterHttps1000k() throws Exception {
        ClientConnection connection = null;
        try {
            SimpleConnectionHolder.ConnectionToken token = client.borrow(new URI("https://localhost:8000"), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true));

            connection = (ClientConnection) token.getRawConnection();
            final String json = getResourceFileAsString("1000k.json");
            for(int i = 0; i < 100; i++) {
                final CountDownLatch latch = new CountDownLatch(1);
                final AtomicReference<ClientResponse> reference = new AtomicReference<>();
                final ClientRequest request = new ClientRequest().setMethod(Methods.POST).setPath("/v1/postData");
                request.getRequestHeaders().put(Headers.HOST, "localhost");
                request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
                request.getRequestHeaders().put(Headers.CONTENT_TYPE, "application/json");
                connection.sendRequest(request, client.createClientCallback(reference, latch, json));
                latch.await();
                int statusCode = reference.get().getResponseCode();
                String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
                if(statusCode != 200) System.out.println("testRouter4k failed with statusCode = " + statusCode + " body = " + body);
                System.out.println(i);
                if(!body.startsWith("[")) System.out.println(body);
            }
        } finally {
            client.restore(token);
        }
    }

    /**
     * This is direct call to the post-service with a 48k json as body
     * on 8080 which the post-router is listening.
     *
     * @throws Exception
     */
    public void testRouterHttp48k() throws Exception {
        ClientConnection connection = null;
        try {
            SimpleConnectionHolder.ConnectionToken token = client.borrow(new URI("http://localhost:8000"), Http2Client.WORKER, Http2Client.BUFFER_POOL, OptionMap.EMPTY);

            connection = (ClientConnection) token.getRawConnection();
            final String json = getResourceFileAsString("48k.json");
            if(logger.isDebugEnabled()) logger.debug(json);
            for(int i = 0; i < 100; i++) {
                final CountDownLatch latch = new CountDownLatch(1);
                final AtomicReference<ClientResponse> reference = new AtomicReference<>();
                final ClientRequest request = new ClientRequest().setMethod(Methods.POST).setPath("/v1/postData");
                request.getRequestHeaders().put(Headers.HOST, "localhost");
                request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
                request.getRequestHeaders().put(Headers.CONTENT_TYPE, "application/json");
                connection.sendRequest(request, client.createClientCallback(reference, latch, json));
                latch.await(100, TimeUnit.MILLISECONDS);
                int statusCode = reference.get().getResponseCode();
                String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
                if(statusCode != 200) System.out.println("testRouter4k failed with statusCode = " + statusCode + " body = " + body);
                System.out.println(body);
            }
        } finally {
            client.restore(token);
        }
    }

    /**
     * This is indirect call to the undertow reverse proxy with a 4k json as body
     * on 8080 which the reverse proxy is listening
     *
     * @throws Exception
     */
    public void testProxy4k() throws Exception {
        ClientConnection connection = null;
        try {
            SimpleConnectionHolder.ConnectionToken token = client.borrow(new URI("http://localhost:8000"), Http2Client.WORKER, Http2Client.BUFFER_POOL, OptionMap.EMPTY);

            connection = (ClientConnection) token.getRawConnection();
            final String json = getResourceFileAsString("4k.json");
            if(logger.isDebugEnabled()) logger.debug(json);
            for(int i = 0; i < 100; i++) {
                final CountDownLatch latch = new CountDownLatch(1);
                final AtomicReference<ClientResponse> reference = new AtomicReference<>();
                final ClientRequest request = new ClientRequest().setMethod(Methods.POST).setPath("/v1/postData");
                request.getRequestHeaders().put(Headers.HOST, "localhost");
                request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
                request.getRequestHeaders().put(Headers.CONTENT_TYPE, "application/json");
                connection.sendRequest(request, client.createClientCallback(reference, latch, json));
                latch.await(100, TimeUnit.MILLISECONDS);
                int statusCode = reference.get().getResponseCode();
                String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
                if(statusCode != 200) System.out.println("testDirect4k failed with statusCode = " + statusCode + " body = " + body);
                System.out.println(body);
            }
        } finally {
            client.restore(token);
        }

    }

    /**
     * This is indirect call to the post-service with a 48k json as body
     * on 8000 which the reverse proxy is listening.
     *
     * @throws Exception
     */
    public void testProxy48k() throws Exception {
        ClientConnection connection = null;
        try {
            SimpleConnectionHolder.ConnectionToken token = client.borrow(new URI("http://localhost:8000"), Http2Client.WORKER, Http2Client.BUFFER_POOL, OptionMap.EMPTY);

            connection = (ClientConnection) token.getRawConnection();
            final String json = getResourceFileAsString("48k.json");
            if(logger.isDebugEnabled()) logger.debug(json);
            for(int i = 0; i < 100; i++) {
                final CountDownLatch latch = new CountDownLatch(1);
                final AtomicReference<ClientResponse> reference = new AtomicReference<>();
                final ClientRequest request = new ClientRequest().setMethod(Methods.POST).setPath("/v1/postData");
                request.getRequestHeaders().put(Headers.HOST, "localhost");
                request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
                request.getRequestHeaders().put(Headers.CONTENT_TYPE, "application/json");
                connection.sendRequest(request, client.createClientCallback(reference, latch, json));
                latch.await(100, TimeUnit.MILLISECONDS);
                int statusCode = reference.get().getResponseCode();
                String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
                if(statusCode != 200) System.out.println("testDirect4k failed with statusCode = " + statusCode + " body = " + body);
                System.out.println(body);
                System.out.println(i);
            }
        } finally {
            client.restore(token);
        }
    }

    /**
     * Reads given resource file as a string.
     *
     * @param fileName the path to the resource file
     * @return the file's contents or null if the file could not be opened
     */
    public String getResourceFileAsString(String fileName) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
        return null;
    }
}
