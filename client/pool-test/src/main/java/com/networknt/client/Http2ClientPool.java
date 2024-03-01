package com.networknt.client;

import com.networknt.client.simplepool.SimpleConnectionHolder;
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
//        for(int i = 0; i < 10000; i++) {
//            e.testHttp2Get();
//            Thread.sleep(1000);
//        }
        // e.testHttp2Post();
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
        SimpleConnectionHolder.ConnectionToken connectionToken = null;
        URI uri = new URI("https://localhost:8443");
        final AtomicReference<ClientResponse> reference = new AtomicReference<>();
        try {
            connectionToken = client.borrow(uri, Http2Client.WORKER, client.getDefaultXnioSsl(), Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true));
            ClientConnection connection = (ClientConnection) connectionToken.getRawConnection();
            final ClientRequest request = new ClientRequest().setMethod(Methods.GET).setPath("/v1/pets");
            request.getRequestHeaders().put(Headers.HOST, "localhost");
            connection.sendRequest(request, client.createClientCallback(reference, latch));
            latch.await(100, TimeUnit.MILLISECONDS);
        } finally {
            client.restore(connectionToken);
        }
        int statusCode = reference.get().getResponseCode();
        String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
        System.out.println("statusCode = " + statusCode + " body = " + body);
    }

    public void testHttp2Post() throws Exception {
        // Create one CountDownLatch that will be reset in the callback function
        final CountDownLatch latch = new CountDownLatch(1);
        SimpleConnectionHolder.ConnectionToken connectionToken = null;
        URI uri = new URI("https://localhost:8443");
        final AtomicReference<ClientResponse> reference = new AtomicReference<>();
        String requestBody = "{\"id\":1,\"name\":\"doggie\"}";
        try {
            connectionToken = client.borrow(uri, Http2Client.WORKER, client.getDefaultXnioSsl(), Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true));
            ClientConnection connection = (ClientConnection) connectionToken.getRawConnection();
            final ClientRequest request = new ClientRequest().setMethod(Methods.POST).setPath("/v1/pets");
            request.getRequestHeaders().put(Headers.HOST, "localhost");
            request.getRequestHeaders().put(Headers.CONTENT_TYPE, "application/json");
            request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
            connection.sendRequest(request, client.createClientCallback(reference, latch, requestBody));
            latch.await(100, TimeUnit.MILLISECONDS);
        } finally {
            client.restore(connectionToken);
        }
        int statusCode = reference.get().getResponseCode();
        String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
        System.out.println("statusCode = " + statusCode + " body = " + body);
    }

}
