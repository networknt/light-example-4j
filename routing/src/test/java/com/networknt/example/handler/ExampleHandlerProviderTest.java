package com.networknt.example.handler;

import com.networknt.server.Server;
import com.sun.deploy.net.HttpResponse;
import io.undertow.util.StatusCodes;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.*;

import java.io.IOException;

/**
 * Created by steve on 02/09/16.
 */
public class ExampleHandlerProviderTest {
    private static Server server = null;

    @Before
    public void setUp() {
        if (server != null) {
            return;
        }
        System.out.println("starting server");
        server = new Server();
        server.start();
    }

    @Test
    public void testRoutingTemplateHandler() throws IOException {
        Assert.assertTrue(true);
    }


    @Test
    public void testFalse() throws IOException {
        Assert.assertFalse(false);
    }

}
