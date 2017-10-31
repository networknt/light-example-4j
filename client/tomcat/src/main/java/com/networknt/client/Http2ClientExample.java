package com.networknt.client;

import com.networknt.client.oauth.TokenResponse;
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
import org.xnio.IoUtils;
import org.xnio.OptionMap;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.networknt.client.oauth.OauthHelper.encodeCredentials;

/**
 * This is a stand along java client application that is used to demo how to
 * communicate with services built on top of light-*-4j framework through light
 * client model, specifically Http2Client
 *
 * @author Steve Hu
 */
public class Http2ClientExample {
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
        apiHost = cluster.serviceToUrl("http", "io.swagger.swagger-light-java-1.0.0", null, null);
        Http2ClientExample e = new Http2ClientExample();
        e.testHttp2Get();
        System.exit(0);
    }

    /**
     * This is a simple example that create a new HTTP 2.0 connection for get request
     * and close the connection after the call is done. As you can see that it is using
     * a hard coded uri which points to an statically deployed service on fixed ip and port
     *
     * @throws Exception
     */
    public void testHttp2Get() throws Exception {
        // Create one CountDownLatch that will be reset in the callback function
        final CountDownLatch latch = new CountDownLatch(1);
        // Create an HTTP 2.0 connection to the server
        final ClientConnection connection = client.connect(new URI(apiHost), Http2Client.WORKER, Http2Client.POOL, OptionMap.EMPTY).get();
        // Create an AtomicReference object to receive ClientResponse from callback function
        final AtomicReference<ClientResponse> reference = new AtomicReference<>();
        try {
            //final ClientRequest request = new ClientRequest().setMethod(Methods.GET).setPath("/");
            final ClientRequest request = new ClientRequest().setMethod(Methods.GET).setPath("/");
            request.getRequestHeaders().put(Headers.HOST, "localhost");

            // this is to ask client module to pass through correlationId and traceabilityId as well as
            // getting access token from oauth2 server automatically and attatch authorization headers.
            if(securityEnabled) {
                // call OAuth 2.0 provider service to get a JWT access token here and
                // put it into the request header. Optionally, you can put a traceabilityId
                // into the header.
                TokenResponse tokenResponse = getAccessToken();
                // you should check if the token is expired here
                expiration = tokenResponse.getExpiresIn();
                String token = tokenResponse.getAccessToken();
                // traceabilityId should be a uuid or db sequence
                client.addAuthTokenTrace(request, token, "traceabilityId");

            }
            // send request to server with a callback function provided by Http2Client
            connection.sendRequest(request, client.createClientCallback(reference, latch));

            // wait for 100 millisecond to timeout the request.
            latch.await(100, TimeUnit.MILLISECONDS);
        } finally {
            // here the connection is closed after one request. It should be used for in frequent
            // request as creating a new connection is costly with TLS handshake and ALPN.
            IoUtils.safeClose(connection);
        }
        int statusCode = reference.get().getResponseCode();
        String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
        System.out.println("testHttp2Get: statusCode = " + statusCode + " body = " + body);
    }

    /**
     * In this example, I am going to use a customized grant type to get access token. This grant type assumes
     * that the client authenticated the user and send the user info to OAuth 2.0 provider to get an access token.
     * Note that the client must be a strusted client.
     * @return
     */
    private TokenResponse getAccessToken() throws Exception {
        TokenResponse tokenResponse = null;
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "client_authenticated_user");
        params.put("userId", "admin");
        params.put("userType", "Employee");
        params.put("transit", "12345"); // This is the custom claim that need to be shown in JWT token.
        String s = Http2Client.getFormDataString(params);
        final AtomicReference<ClientResponse> reference = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        final ClientConnection connection;
        try {
            // all the connection information should be from client.yml
            connection = client.connect(new URI("https://localhost:6882"), Http2Client.WORKER, Http2Client.SSL, Http2Client.POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true)).get();
            final ClientRequest request = new ClientRequest().setMethod(Methods.POST).setPath("/oauth2/token");
            request.getRequestHeaders().put(Headers.HOST, "localhost");
            request.getRequestHeaders().put(Headers.AUTHORIZATION, "Basic " + encodeCredentials("f7d42348-c647-4efb-a52d-4c5787421e72", "f6h1FTI8Q3-7UScPZDzfXA"));
            request.getRequestHeaders().put(Headers.CONTENT_TYPE, "application/x-www-form-urlencoded");
            request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
            connection.sendRequest(request, client.createClientCallback(reference, latch, s));
            latch.await(1, TimeUnit.SECONDS);
            String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
            tokenResponse = Config.getInstance().getMapper().readValue(body, TokenResponse.class);
        } catch (Exception e) {
            throw new ClientException(e);
        }

        return tokenResponse;
    }

}
