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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathHandlerProvider implements HandlerProvider {
    static final Logger logger = LoggerFactory.getLogger(PathHandlerProvider.class);

    String apidUrl = "http://localhost:8083/v1/data";
    public HttpHandler getHandler() {
        HttpHandler handler = Handlers.routing()


            .add(Methods.GET, "/v1/data", new HttpHandler() {
                        public void handleRequest(HttpServerExchange exchange) throws Exception {
                            // get passed in Authorization header
                            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

                            List<String> list = new ArrayList<String>();
                            try {
                                CloseableHttpClient client = Client.getInstance().getSyncClient();
                                HttpGet httpGet = new HttpGet(apidUrl);
                                Client.getInstance().addAuthorizationWithScopeToken(httpGet, authHeader);
                                CloseableHttpResponse response = client.execute(httpGet);
                                int responseCode = response.getStatusLine().getStatusCode();
                                if(responseCode != 200){
                                    throw new Exception("Failed to call API D: " + responseCode);
                                }
                                List<String> apidList = (List<String>) Config.getInstance().getMapper().readValue(response.getEntity().getContent(),
                                        new TypeReference<List<String>>(){});
                                list.addAll(apidList);
                            } catch (ClientException e) {
                                    throw new Exception("Client Exception: ", e);
                            } catch (IOException e) {
                                    throw new Exception("IOException:", e);
                            }
                            // now add API B specific messages
                            list.add("API B: Message 1");
                            list.add("API B: Message 2");
                            exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(list));
                        }
                    })

        ;
        return handler;
    }
}

