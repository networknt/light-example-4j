
package com.networknt.eventuate.transaction.command.handler;

import com.networknt.client.Client;
import com.networknt.eventuate.account.common.model.account.CreateAccountRequest;
import com.networknt.eventuate.account.common.model.transaction.CreateMoneyTransferRequest;
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
import java.math.BigDecimal;


public class TransfersPostHandlerTest {
    @ClassRule
    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(TransfersPostHandlerTest.class);

    @Test
    public void testTransfersPostHandlerTest() throws ClientException, ApiException {
        CloseableHttpClient client = Client.getInstance().getSyncClient();
        HttpPost httpPost = new HttpPost ("http://localhost:8085/v1/transfers");
        CreateMoneyTransferRequest request = new CreateMoneyTransferRequest();
        request.setFromAccountId("2222-22222");
        request.setToAccountId("2222-22223");
        request.setAmount(new BigDecimal(23555L));
        request.setDescription("test");
        String json = JSonMapper.toJson(request);
        System.out.println(json);
      //  Client.getInstance().addAuthorization(httpPost);
        try {
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());
            //Assert.assertEquals("", IOUtils.toString(response.getEntity().getContent(), "utf8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
