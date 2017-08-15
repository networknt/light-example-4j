
package com.networknt.eventuate.customer.command.handler;

import com.networknt.client.Client;
import com.networknt.eventuate.account.common.model.account.CreateAccountRequest;
import com.networknt.eventuate.account.common.model.customer.Address;
import com.networknt.eventuate.account.common.model.customer.CustomerInfo;
import com.networknt.eventuate.account.common.model.customer.Name;
import com.networknt.eventuate.account.common.model.customer.UserCredentials;
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


public class CreatecustomerPostHandlerTest {
    @ClassRule
    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(CreatecustomerPostHandlerTest.class);

    @Test
    public void testCreatecustomerPostHandlerTest() throws ClientException, ApiException {
        CloseableHttpClient client = Client.getInstance().getSyncClient();
        HttpPost httpPost = new HttpPost ("http://localhost:8083/v1/createcustomer");
        Name name = new Name("Google", "Com");
        Address address = new Address("Yonge St" , "2556 unit", "toronto", "ON", "Canada", "L3R, 5F5");
        UserCredentials userCredentials = new UserCredentials ("aaa.bbb@google.com", "password");
        CustomerInfo customerInfo = new CustomerInfo(name, userCredentials, "9999999999", "4166666666", address);

        String json = JSonMapper.toJson(customerInfo);
        System.out.println(json);

        //  Client.getInstance().addAuthorization(httpPost);
        try {
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            String body = IOUtils.toString(response.getEntity().getContent());
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());
            System.out.println("result:" + body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
