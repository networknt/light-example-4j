package com.networknt.apid.handler;

import com.networknt.client.Client;
import com.networknt.server.Server;
import com.networknt.exception.ClientException;
import com.networknt.exception.ApiException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class DataGetHandlerTest {
    @ClassRule
    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(DataGetHandlerTest.class);

    @Test
    public void testDataGetHandlerHttp() throws ClientException, ApiException {
        CloseableHttpClient client = Client.getInstance().getSyncClient();
        HttpGet httpGet = new HttpGet ("http://localhost:7004/v1/data");
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String body = IOUtils.toString(response.getEntity().getContent(), "utf8");
            Assert.assertEquals(200, statusCode);
            Assert.assertNotNull(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDataGetHandlerHttps() throws ClientException, ApiException {
        CloseableHttpClient client = Client.getInstance().getSyncClient();
        HttpGet httpGet = new HttpGet ("https://localhost:7444/v1/data");
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String body = IOUtils.toString(response.getEntity().getContent(), "utf8");
            Assert.assertEquals(200, statusCode);
            Assert.assertNotNull(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
