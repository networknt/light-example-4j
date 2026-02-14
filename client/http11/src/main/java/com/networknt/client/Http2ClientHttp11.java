package com.networknt.client;

import com.networknt.client.simplepool.SimpleConnectionState;
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

/**
 * This is a client to call the petstore API with HTTPS and HTTP/1.1 constantly
 * to ensure that connections in OK.
 *
 * @author Steve Hu
 */
public class Http2ClientHttp11 {
    static Http2Client client = Http2Client.getInstance();

    public static void main(String[] args) throws Exception {
        Http2ClientHttp11 e = new Http2ClientHttp11();
        for (int i = 0; i < 10000; i++) {
            e.testHttp2Get();
            Thread.sleep(1000);
        }
        System.exit(0);
    }

    /**
     * This is a simple example that create a new HTTP 2.0 connection for get
     * request
     * and close the connection after the call is done. As you can see that it is
     * using
     * a hard coded uri which points to an statically deployed service on fixed ip
     * and port
     *
     * @throws Exception
     */
    public void testHttp2Get() throws Exception {
        // Create one CountDownLatch that will be reset in the callback function
        final CountDownLatch latch = new CountDownLatch(1);
        // Create an HTTP 2.0 connection to the server using SimplePool
        SimpleConnectionState.ConnectionToken token = client.borrow((new URI("https://localhost:9443")),
                Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL,
                OptionMap.create(UndertowOptions.ENABLE_HTTP2, true));
        final ClientConnection connection = (ClientConnection) token.getRawConnection();
        // Create an AtomicReference object to receive ClientResponse from callback
        // function
        final AtomicReference<ClientResponse> reference = new AtomicReference<>();
        try {
            final ClientRequest request = new ClientRequest().setMethod(Methods.GET).setPath("/v1/pets");
            request.getRequestHeaders().put(Headers.HOST, "localhost");
            connection.sendRequest(request, client.createClientCallback(reference, latch));
            latch.await(1000, TimeUnit.MILLISECONDS);
        } finally {
            client.restore(token);
        }
        int statusCode = reference.get().getResponseCode();
        String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
        System.out.println("statusCode = " + statusCode + " body = " + body);
    }
}
