
package com.networknt.todolist.query.handler;

import com.networknt.client.Client;
import com.networknt.server.Server;
import com.networknt.exception.ClientException;
import com.networknt.exception.ApiException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetAllTodosTest {
    @ClassRule
    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(GetAllTodos.class);

    @Test
    public void testGetAllTodos() throws ClientException, ApiException {
        CloseableHttpClient client = Client.getInstance().getSyncClient();
        HttpPost httpPost = new HttpPost("http://localhost:8080/api/json");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("host", "lightapi.net");
        map.put("service", "todo");
        map.put("action", "gettodos");
        map.put("version", "0.1.0");

        JSONObject json = new JSONObject();
        json.putAll( map );
        System.out.printf( "JSON: %s", json.toString() );
        //Client.getInstance().addAuthorization(httpPost);
        try {
            httpPost.setEntity(new StringEntity(json.toString()));
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}