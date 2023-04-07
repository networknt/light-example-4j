package com.networknt.client;

import io.undertow.UndertowOptions;
import io.undertow.client.ClientConnection;
import io.undertow.client.ClientRequest;
import io.undertow.client.ClientResponse;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import org.xnio.OptionMap;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static io.undertow.client.http.HttpClientProvider.DISABLE_HTTPS_ENDPOINT_IDENTIFICATION_PROPERTY;

/**
 * This is a client to call the petstore API constantly to ensure that connections in the pool is not growing.
 *
 * @author Steve Hu
 */
public class Http2ClientPool {
    static Http2Client client;

    public static void main(String[] args) throws Exception {
        //System.setProperty(DISABLE_HTTPS_ENDPOINT_IDENTIFICATION_PROPERTY, "true");
        client = Http2Client.getInstance();
        Http2ClientPool e = new Http2ClientPool();
        for(int i = 0; i < 10000; i++) {
            e.testHttp2Get();
            Thread.sleep(1000);
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
        // Create one CountDownLatch that will be reset in the callback function
        final CountDownLatch latch = new CountDownLatch(1);
        // Create an HTTP 2.0 connection to the server
        //final ClientConnection connection = client.borrowConnection((new URI("https://lucky:9443")), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true, UndertowOptions.ENDPOINT_IDENTIFICATION_ALGORITHM, "")).get();
        final ClientConnection connection = client.borrowConnection((new URI("https://lucky:9443")), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true)).get();
        // Create an AtomicReference object to receive ClientResponse from callback function
        final AtomicReference<ClientResponse> reference = new AtomicReference<>();
        try {
            final ClientRequest request = new ClientRequest().setMethod(Methods.GET).setPath("/v1/pets");
            request.getRequestHeaders().put(Headers.HOST, "localhost");
            connection.sendRequest(request, client.createClientCallback(reference, latch));
            latch.await(1000, TimeUnit.MILLISECONDS);
        } finally {
            client.returnConnection(connection);
        }
        int statusCode = reference.get().getResponseCode();
        String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
        System.out.println("statusCode = " + statusCode + " body = " + body);
    }
}
