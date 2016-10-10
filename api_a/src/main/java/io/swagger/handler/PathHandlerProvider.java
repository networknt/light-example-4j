package io.swagger.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.networknt.client.Client;
import com.networknt.config.Config;
import com.networknt.exception.ClientException;
import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PathHandlerProvider implements HandlerProvider {

    public HttpHandler getHandler() {
        HttpHandler handler = Handlers.routing()


            .add(Methods.GET, "/v1/data", new HttpHandler() {
                        public void handleRequest(HttpServerExchange exchange) throws Exception {
                            List<String> list = new ArrayList<String>();
                            final HttpGet[] requests = new HttpGet[] {
                                    new HttpGet("http://localhost:8081/v1/data"),
                                    new HttpGet("http://localhost:8082/v1/data"),
                            };
                            try {
                                CloseableHttpAsyncClient client = Client.getInstance().getAsyncClient();
                                final CountDownLatch latch = new CountDownLatch(requests.length);
                                for (final HttpGet request: requests) {
                                    client.execute(request, new FutureCallback<HttpResponse>() {
                                        @Override
                                        public void completed(final HttpResponse response) {
                                            latch.countDown();
                                            try {
                                                List<String> apiList = (List<String>) Config.getInstance().getMapper().readValue(response.getEntity().getContent(),
                                                        new TypeReference<List<String>>(){});
                                                list.addAll(apiList);
                                            } catch (IOException e) {

                                            }
                                        }

                                        @Override
                                        public void failed(final Exception ex) {
                                            latch.countDown();
                                        }

                                        @Override
                                        public void cancelled() {
                                            latch.countDown();
                                        }
                                    });
                                }
                                latch.await();
                            } catch (ClientException e) {
                                throw new Exception("ClientException:", e);
                            }
                            // now add API A specific messages
                            list.add("API A: Message 1");
                            list.add("API A: Message 2");
                            exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(list));
                        }
                    })

        ;
        return handler;
    }
}

