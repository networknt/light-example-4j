package com.networknt.client;

import ch.qos.logback.core.net.server.Client;
import com.networknt.client.oauth.TokenResponse;
import com.networknt.client.simplepool.SimpleConnectionState;
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
        e.testHttpTableauGoogleLocalhost();
        e.testHttpTableauGoogle();
        e.testHttpLocalhost();
        System.exit(0);
    }

    /**
     * This is a simple example that create a new HTTP 2.0 connection for get request
     * and close the connection after the call is done. As you can see that it is using
     * a hard coded uri which points to an statically deployed service on fixed ip and port
     *
     * @throws Exception
     */
    public void testHttpTableauGoogle() throws Exception {
        // Create one CountDownLatch that will be reset in the callback function
        final CountDownLatch latch = new CountDownLatch(2);

        SimpleConnectionState.ConnectionToken connectionTokenTableau = null;
        SimpleConnectionState.ConnectionToken connectionTokenGoogle = null;


        // Create an AtomicReference object to receive ClientResponse from callback function
        final AtomicReference<ClientResponse> referenceTableau = new AtomicReference<>();
        final AtomicReference<ClientResponse> referenceGoogle = new AtomicReference<>();
        try {
            connectionTokenTableau = client.borrow(new URI("https://us-east-1.online.tableau.com"), Http2Client.WORKER, client.getDefaultXnioSsl(), Http2Client.BUFFER_POOL, OptionMap.EMPTY);
            ClientConnection connectionTableau = (ClientConnection) connectionTokenTableau.getRawConnection();
            connectionTokenGoogle = client.borrow(new URI("https://google.com"), Http2Client.WORKER, client.getDefaultXnioSsl(), Http2Client.BUFFER_POOL, OptionMap.EMPTY);
            ClientConnection connectionGoogle = (ClientConnection) connectionTokenGoogle.getRawConnection();

            ClientRequest request = new ClientRequest().setMethod(Methods.GET).setPath("/");
            request.getRequestHeaders().put(Headers.HOST, "localhost");
            connectionTableau.sendRequest(request, client.createClientCallback(referenceTableau, latch));
            latch.await(1000, TimeUnit.MILLISECONDS);

            request = new ClientRequest().setMethod(Methods.GET).setPath("/");
            request.getRequestHeaders().put(Headers.HOST, "localhost");
            connectionGoogle.sendRequest(request, client.createClientCallback(referenceGoogle, latch));
            latch.await(1000, TimeUnit.MILLISECONDS);
        } finally {
            client.restore(connectionTokenTableau);
            client.restore(connectionTokenGoogle);
        }
        int statusCode = referenceTableau.get().getResponseCode();
        System.out.println("Tableau  statusCode = " + statusCode);
        String body = referenceTableau.get().getAttachment(Http2Client.RESPONSE_BODY);
        System.out.println("Tableau statusCode = " + statusCode + " body = " + body);

        statusCode = referenceGoogle.get().getResponseCode();
        System.out.println("Google  statusCode = " + statusCode);
        body = referenceGoogle.get().getAttachment(Http2Client.RESPONSE_BODY);
        System.out.println("Google statusCode = " + statusCode + " body = " + body);

    }

    public void testHttpLocalhost() throws Exception {
        // Create one CountDownLatch that will be reset in the callback function
        final CountDownLatch latch = new CountDownLatch(1);
        // Create an HTTP 1.1 connection to the server
        SimpleConnectionState.ConnectionToken connectionToken = null;;
        // Create an AtomicReference object to receive ClientResponse from callback function
        final AtomicReference<ClientResponse> reference = new AtomicReference<>();
        try {
            connectionToken = client.borrow(new URI("https://localhost:9443"), Http2Client.WORKER, client.getDefaultXnioSsl(), Http2Client.BUFFER_POOL, OptionMap.EMPTY);
            ClientConnection connection = (ClientConnection) connectionToken.getRawConnection();
            ClientRequest request = new ClientRequest().setMethod(Methods.GET).setPath("/v1/pets");
            request.getRequestHeaders().put(Headers.HOST, "localhost");
            connection.sendRequest(request, client.createClientCallback(reference, latch));
            latch.await(1000, TimeUnit.MILLISECONDS);
        } finally {
            client.restore(connectionToken);
        }
        int statusCode = reference.get().getResponseCode();
        System.out.println("Localhost  statusCode = " + statusCode);
        String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
        System.out.println("Localhost statusCode = " + statusCode + " body = " + body);

    }

    /**
     * This is a simple example that create a new HTTP 2.0 connection for get request
     * and close the connection after the call is done. As you can see that it is using
     * a hard coded uri which points to an statically deployed service on fixed ip and port
     *
     * @throws Exception
     */
    public void testHttpTableauGoogleLocalhost() throws Exception {
        // Create one CountDownLatch that will be reset in the callback function
        final CountDownLatch latch = new CountDownLatch(3);
        SimpleConnectionState.ConnectionToken connectionTokenTableau = null;
        SimpleConnectionState.ConnectionToken connectionTokenGoogle = null;
        SimpleConnectionState.ConnectionToken connectionTokenLocalhost = null;
        // Create an AtomicReference object to receive ClientResponse from callback function
        final AtomicReference<ClientResponse> referenceTableau = new AtomicReference<>();
        final AtomicReference<ClientResponse> referenceGoogle = new AtomicReference<>();
        final AtomicReference<ClientResponse> referenceLocalhost = new AtomicReference<>();
        try {
            // Create an HTTP 1.1 connection to the server
            connectionTokenTableau = client.borrow(new URI("https://us-east-1.online.tableau.com"), Http2Client.WORKER, client.getDefaultXnioSsl(), Http2Client.BUFFER_POOL, OptionMap.EMPTY);
            ClientConnection connectionTableau = (ClientConnection) connectionTokenTableau.getRawConnection();
            connectionTokenGoogle = client.borrow(new URI("https://google.com"), Http2Client.WORKER, client.getDefaultXnioSsl(), Http2Client.BUFFER_POOL, OptionMap.EMPTY);
            ClientConnection connectionGoogle = (ClientConnection) connectionTokenGoogle.getRawConnection();
            connectionTokenLocalhost = client.borrow(new URI("https://localhost:9443"), Http2Client.WORKER, client.getDefaultXnioSsl(), Http2Client.BUFFER_POOL, OptionMap.EMPTY);
            ClientConnection connectionLocalhost = (ClientConnection) connectionTokenLocalhost.getRawConnection();

            ClientRequest request = new ClientRequest().setMethod(Methods.GET).setPath("/");
            request.getRequestHeaders().put(Headers.HOST, "localhost");
            connectionTableau.sendRequest(request, client.createClientCallback(referenceTableau, latch));
            latch.await(1000, TimeUnit.MILLISECONDS);

            request = new ClientRequest().setMethod(Methods.GET).setPath("/");
            request.getRequestHeaders().put(Headers.HOST, "localhost");
            connectionGoogle.sendRequest(request, client.createClientCallback(referenceGoogle, latch));
            latch.await(1000, TimeUnit.MILLISECONDS);

            request = new ClientRequest().setMethod(Methods.GET).setPath("/v1/pets");
            request.getRequestHeaders().put(Headers.HOST, "localhost");
            connectionLocalhost.sendRequest(request, client.createClientCallback(referenceLocalhost, latch));
            latch.await(1000, TimeUnit.MILLISECONDS);

        } finally {
            client.restore(connectionTokenTableau);
            client.restore(connectionTokenGoogle);
            client.restore(connectionTokenLocalhost);
        }
        int statusCode = referenceTableau.get().getResponseCode();
        System.out.println("Tableau  statusCode = " + statusCode);
        String body = referenceTableau.get().getAttachment(Http2Client.RESPONSE_BODY);
        System.out.println("Tableau statusCode = " + statusCode + " body = " + body);

        statusCode = referenceGoogle.get().getResponseCode();
        System.out.println("Google  statusCode = " + statusCode);
        body = referenceGoogle.get().getAttachment(Http2Client.RESPONSE_BODY);
        System.out.println("Google statusCode = " + statusCode + " body = " + body);

        statusCode = referenceLocalhost.get().getResponseCode();
        System.out.println("Localhost  statusCode = " + statusCode);
        body = referenceLocalhost.get().getAttachment(Http2Client.RESPONSE_BODY);
        System.out.println("Localhost statusCode = " + statusCode + " body = " + body);

    }

}
