package com.networknt.client;

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
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.IoUtils;
import org.xnio.OptionMap;

import java.io.IOException;
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
    static final Logger logger = LoggerFactory.getLogger(Http2ClientExample.class);
    // Get the singleton Http2Client instance
    static Http2Client client = Http2Client.getInstance();

    public static void main(String[] args) {
        Http2ClientExample e = new Http2ClientExample();
        try {
            e.testHttp2Get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * This is a simple example that create a new HTTP 2.0 connection for get request
     * and close the connection after the call is done. As you can see that it is using
     * a hard coded uri which points to a statically deployed service on fixed ip and port
     *
     */
    public void testHttp2Get() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        SimpleConnectionState.ConnectionToken connectionToken = null;

        final AtomicReference<ClientResponse> reference = new AtomicReference<>();
        boolean completed  = false;
        try {
            connectionToken = client.borrow(new URI("https://localhost:9445"), Http2Client.WORKER, client.getDefaultXnioSsl(), Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true));
            ClientConnection connection = (ClientConnection) connectionToken.getRawConnection();
            ClientRequest request = new ClientRequest().setPath("/v1/pets").setMethod(Methods.GET);
            request.getRequestHeaders().put(Headers.CONTENT_TYPE, "application/json");
            request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
            request.getRequestHeaders().put(new HttpString("host"), "localhost");
            connection.sendRequest(request, client.createClientCallback(reference, latch));
            completed = latch.await(ClientConfig.get().getTimeout(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.error("InterruptedException:", e);
            throw new ClientException(e);
        } finally {
            client.restore(connectionToken);
        }
        if(!completed) {
            logger.error("Request timeout");
            throw new ClientException("Request timeout");
        } else {
            int statusCode = reference.get().getResponseCode();
            String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
            System.out.println("testHttp2Get: statusCode = " + statusCode + " body = " + body);
        }
    }
}
