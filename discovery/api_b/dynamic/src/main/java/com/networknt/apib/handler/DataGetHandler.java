package com.networknt.apib.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.networknt.client.Client;
import com.networknt.cluster.Cluster;
import com.networknt.config.Config;
import com.networknt.exception.ClientException;
import com.networknt.service.SingletonServiceFactory;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataGetHandler implements HttpHandler {
    private static Logger logger = LoggerFactory.getLogger(DataGetHandler.class);
    private static Cluster cluster = (Cluster) SingletonServiceFactory.getBean(Cluster.class);

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        List<String> list = new ArrayList<>();
        String apidUrl = cluster.serviceToUrl("http", "com.networknt.apid-1.0.0") + "/v1/data";
        if(logger.isDebugEnabled()) logger.debug("apidUrl = " + apidUrl);

        try {
            CloseableHttpClient client = Client.getInstance().getSyncClient();
            HttpGet httpGet = new HttpGet(apidUrl);
            //Client.getInstance().propagateHeaders(httpGet, exchange);
            CloseableHttpResponse response = client.execute(httpGet);
            int responseCode = response.getStatusLine().getStatusCode();
            if(responseCode != 200){
                throw new Exception("Failed to call API D: " + responseCode);
            }
            List<String> apidList = Config.getInstance().getMapper().readValue(response.getEntity().getContent(),
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
}
