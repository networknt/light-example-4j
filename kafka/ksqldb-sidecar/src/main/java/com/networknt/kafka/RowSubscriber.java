package com.networknt.kafka;

import com.networknt.client.ClientConfig;
import com.networknt.client.Http2Client;
import com.networknt.kafka.handler.QueryUserIdGetHandler;
import io.confluent.ksql.api.client.Row;
import io.undertow.UndertowOptions;
import io.undertow.client.ClientConnection;
import io.undertow.client.ClientRequest;
import io.undertow.client.ClientResponse;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.OptionMap;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class RowSubscriber implements Subscriber<Row> {
    private static final Logger logger = LoggerFactory.getLogger(RowSubscriber.class);
    static Http2Client client = Http2Client.getInstance();
    private ClientConnection connection;
    private Subscription subscription;

    public RowSubscriber() {
    }

    @Override
    public synchronized void onSubscribe(Subscription subscription) {
        System.out.println("Subscriber is subscribed.");
        this.subscription = subscription;
        // Request the first row
        subscription.request(1);
    }

    @Override
    public synchronized void onNext(Row row) {
        System.out.println("Received a row!");
        System.out.println("Row: " + row.values());
        // send the row to the ksqldb-backend instance
        if(connection == null || !connection.isOpen()) {
            try {
                connection = client.borrowConnection(new URI("https://localhost:8444"), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true)).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<ClientResponse> reference = new AtomicReference<>();
        try {
            ClientRequest request = new ClientRequest().setMethod(Methods.POST).setPath("/kafka/ksqldb");
            request.getRequestHeaders().put(Headers.CONTENT_TYPE, "application/json");
            request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
            connection.sendRequest(request, client.createClientCallback(reference, latch, row.values().toString()));
            latch.await();
            int statusCode = reference.get().getResponseCode();
            String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
            logger.debug("statusCode = " + statusCode);
            logger.debug("body = " + body);
        } catch (Exception  e) {
            e.printStackTrace();
        }
        // Request the next row
        subscription.request(1);
    }

    @Override
    public synchronized void onError(Throwable t) {
        System.out.println("Received an error: " + t);
    }

    @Override
    public synchronized void onComplete() {
        System.out.println("Query has ended.");
    }
}
