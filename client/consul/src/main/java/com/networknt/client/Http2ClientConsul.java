package com.networknt.client;

import com.networknt.cluster.Cluster;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This is a stand along java client application that is used to reproduce one of the
 * issues opened by users. This client will connect to the Kubernetes cluster and
 * Consul cluster for service discovery. The business logic is very similar with the
 * standalone example but the configuration would be different.
 *
 * As the focus is to test Consul service discovery, OAuth is disabled here.
 *
 * @author Steve Hu
 */
public class Http2ClientConsul {
    // Get the singleton Http2Client instance/home/steve/networknt/light-example-4j/discovery/api_a/http-health/src/main/resources/config/client.truststore
    static Http2Client client = Http2Client.getInstance();

    public static void main(String[] args) throws Exception {
        Http2ClientConsul e = new Http2ClientConsul();
        for(int i = 0; i < 1000; i++) {
            e.testHttp2Get();
            Thread.sleep(5000);
        }
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
        // Get the singleton Cluster instance
        Cluster cluster = SingletonServiceFactory.getBean(Cluster.class);
        // Do the service discovery for every call to reproduce the issue
        String apiaHost = cluster.serviceToUrl("https", "com.networknt.apia-1.0.0", null, null);
        // Create one CountDownLatch that will be reset in the callback function
        final CountDownLatch latch = new CountDownLatch(1);
        // Create an HTTP 2.0 connection to the server
        final ClientConnection connection = client.connect(new URI(apiaHost), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true)).get();
        // Create an AtomicReference object to receive ClientResponse from callback function
        final AtomicReference<ClientResponse> reference = new AtomicReference<>();
        try {
            final ClientRequest request = new ClientRequest().setMethod(Methods.GET).setPath("/v1/data");
            request.getRequestHeaders().put(Headers.HOST, "localhost");
            // send request to server with a callback function provided by Http2Client
            connection.sendRequest(request, client.createClientCallback(reference, latch));
            // wait for 100 millisecond to timeout the request.
            latch.await(1000, TimeUnit.MILLISECONDS);
        } finally {
            // here the connection is closed after one request. It should be used for in frequent
            // request as creating a new connection is costly with TLS handshake and ALPN.
            IoUtils.safeClose(connection);
        }
        int statusCode = reference.get().getResponseCode();
        String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
        System.out.println("testHttp2Get: statusCode = " + statusCode + " body = " + body);
    }

}
