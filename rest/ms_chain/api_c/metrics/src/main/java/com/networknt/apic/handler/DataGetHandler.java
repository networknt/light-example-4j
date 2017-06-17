package com.networknt.apic.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.networknt.client.Client;
import com.networknt.config.Config;
import com.networknt.exception.ClientException;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataGetHandler implements HttpHandler {
    static String CONFIG_NAME = "api_c";
    static String apidUrl = (String) Config.getInstance().getJsonMapConfig(CONFIG_NAME).get("api_d_endpoint");

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        List<String> list = new ArrayList<String>();
        try {
            CloseableHttpClient client = Client.getInstance().getSyncClient();
            HttpGet httpGet = new HttpGet(apidUrl);
            Client.getInstance().propagateHeaders(httpGet, exchange);
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
        list.add("API C: Message 1");
        list.add("API C: Message 2");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(list));
    }
}
