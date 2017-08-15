
package com.networknt.eventuate.customer.command.handler;

import com.networknt.client.Client;
import com.networknt.eventuate.account.common.model.customer.ToAccountInfo;
import com.networknt.eventuate.common.impl.JSonMapper;
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
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class CustomersToaccountsIdPostHandlerTest {
    @ClassRule
    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(CustomersToaccountsIdPostHandlerTest.class);

    @Test
    public void testCustomersToaccountsIdPostHandlerTest() throws ClientException, ApiException {
        CloseableHttpClient client = Client.getInstance().getSyncClient();
        HttpPost httpPost = new HttpPost ("http://localhost:8083/v1/customers/toaccounts/1");

        ToAccountInfo toAccountInfo = new ToAccountInfo("122222", "title", "google", "trest case");
        String json = JSonMapper.toJson(toAccountInfo);
        System.out.println(json);

      //  Client.getInstance().addAuthorization(httpPost);
        try {
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Content-type", "application/json");
      //      CloseableHttpResponse response = client.execute(httpPost);
       //     int statusCode = response.getStatusLine().getStatusCode();
         //   String body = IOUtils.toString(response.getEntity().getContent(), "utf8");
      //      System.out.println("body = " + body);
      //      Assert.assertEquals(200, statusCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
