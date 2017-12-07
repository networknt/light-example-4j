
package com.networknt.eventuate.customer.command.handler;


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


public class ToaccountsCustomerIdAccountIdDeleteHandlerTest {
    @ClassRule
    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(ToaccountsCustomerIdAccountIdDeleteHandlerTest.class);
    static final boolean enableHttp2 = server.getServerConfig().isEnableHttp2();
    static final boolean enableHttps = server.getServerConfig().isEnableHttps();
    static final int httpPort = server.getServerConfig().getHttpPort();
    static final int httpsPort = server.getServerConfig().getHttpsPort();
    static final String url = enableHttp2 || enableHttps ? "https://localhost:" + httpsPort : "http://localhost:" + httpPort;

    @Test
    public void testToaccountsCustomerIdAccountIdDeleteHandlerTest() throws ClientException, ApiException {



        //HttpDelete httpDelete = new HttpDelete ("http://localhost:8083/v1/toaccounts/122222/2222-222");

      //  Client.getInstance().addAuthorization(httpDelete);
        try {
    //        CloseableHttpResponse response = client.execute(httpDelete);
     //       Assert.assertEquals(200, response.getStatusLine().getStatusCode());
         //   Assert.assertEquals("", IOUtils.toString(response.getEntity().getContent(), "utf8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
