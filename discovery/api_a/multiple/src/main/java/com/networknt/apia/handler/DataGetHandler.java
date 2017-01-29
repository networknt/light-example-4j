package com.networknt.apia.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.networknt.client.Client;
import com.networknt.cluster.Cluster;
import com.networknt.config.Config;
import com.networknt.exception.ClientException;
import com.networknt.service.SingletonServiceFactory;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataGetHandler implements HttpHandler {
    private static Logger logger = LoggerFactory.getLogger(DataGetHandler.class);
    private static Cluster cluster = (Cluster) SingletonServiceFactory.getBean(Cluster.class);

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        List<String> list = new Vector<>();

        String apibUrl = cluster.serviceToUrl("http", "com.networknt.apib-1.0.0") + "/v1/data";
        if(logger.isDebugEnabled()) logger.debug("apibUrl = " + apibUrl);
        String apicUrl = cluster.serviceToUrl("http", "com.networknt.apic-1.0.0") + "/v1/data";
        if(logger.isDebugEnabled()) logger.debug("apicUrl = " + apicUrl);
        final HttpGet[] requests = new HttpGet[] {
                new HttpGet(apibUrl),
                new HttpGet(apicUrl),
        };
        try {
            CloseableHttpAsyncClient client = Client.getInstance().getAsyncClient();
            final CountDownLatch latch = new CountDownLatch(requests.length);
            for (final HttpGet request: requests) {
                //Client.getInstance().propagateHeaders(request, exchange);
                client.execute(request, new FutureCallback<HttpResponse>() {
                    @Override
                    public void completed(final HttpResponse response) {
                        try {
                            List<String> apiList = Config.getInstance().getMapper().readValue(response.getEntity().getContent(),
                                    new TypeReference<List<String>>(){});
                            list.addAll(apiList);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        latch.countDown();
                    }

                    @Override
                    public void failed(final Exception ex) {
                        ex.printStackTrace();
                        latch.countDown();
                    }

                    @Override
                    public void cancelled() {
                        System.out.println("cancelled");
                        latch.countDown();
                    }
                });
            }
            latch.await();
        } catch (ClientException e) {
            e.printStackTrace();
            throw new Exception("ClientException:", e);
        }
        // now add API A specific messages
        list.add("API A: Message 1");
        list.add("API A: Message 2");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(list));
    }
}
