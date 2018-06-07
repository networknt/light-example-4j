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
 * This is a stand along java client application that is used to demo how to communicate
 * tableau online service with Http2Client. As the target site is in HTTPS, we need to
 * download the certificate from the site and put it into client.truststore in order to
 * make the TLS handshake work.
 *
 * The detailed steps can be found at www.networknt.com/tutorial/
 *
 * @author Steve Hu
 */
public class Http2ClientExample {
    // Get the singleton Http2Client instance
    static Http2Client client = Http2Client.getInstance();

    public static void main(String[] args) throws Exception {
        Http2ClientExample e = new Http2ClientExample();
        e.testHttpGet();
        System.exit(0);
    }

    /**
     * This is a simple example that create a new HTTP 2.0 connection for get request
     * and close the connection after the call is done. As you can see that it is using
     * a hard coded uri which points to an statically deployed service on fixed ip and port
     *
     * @throws Exception
     */
    public void testHttpGet() throws Exception {
        // Create one CountDownLatch that will be reset in the callback function
        final CountDownLatch latch = new CountDownLatch(1);
        // Create an HTTP 1.1 connection to the server
        final ClientConnection connection = client.connect(new URI("https://us-east-1.online.tableau.com"), Http2Client.WORKER, Http2Client.SSL, Http2Client.POOL, OptionMap.EMPTY).get();
        // Create an AtomicReference object to receive ClientResponse from callback function
        final AtomicReference<ClientResponse> reference = new AtomicReference<>();
        try {
            final ClientRequest request = new ClientRequest().setMethod(Methods.GET).setPath("/");
            // send request to server with a callback function provided by Http2Client
            request.getRequestHeaders().put(Headers.HOST, "localhost");
            connection.sendRequest(request, client.createClientCallback(reference, latch));
            // wait for 100 millisecond to timeout the request.
            latch.await(1000, TimeUnit.MILLISECONDS);
        } finally {
            // here the connection is closed after one request. It should be used for in frequent
            // request as creating a new connection is costly with TLS handshake and ALPN.
            IoUtils.safeClose(connection);
        }
        int statusCode = reference.get().getResponseCode();
        System.out.println("statusCode = " + statusCode);
        String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
        System.out.println("testHttp2Get: statusCode = " + statusCode + " body = " + body);
    }
}
