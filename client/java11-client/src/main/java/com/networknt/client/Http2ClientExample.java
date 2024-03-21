package com.networknt.client;


import com.networknt.http.client.HttpClientRequest;
import com.networknt.http.client.HttpMethod;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * This is a stand along java client application that is used to demo how to
 * communicate with services built on top of light-*-4j framework through light
 * client model, specifically Http2Client
 *
 * @author Steve Hu
 */
public class Http2ClientExample {

    public static void main(String[] args) throws Exception {
        Http2ClientExample e = new Http2ClientExample();
        e.testHttpGet();
        e.testHttpPost();
        System.exit(0);
    }

    /**
     * This is a simple example that create a new HTTP 2.0 connection for get request
     * and close the connection after the call is done. As you can see that it is using
     * a hard coded uri which points to an statically deployed service on fixed ip and port
     *
     * @throws Exception
     */
    public void testHttpGet() throws Exception {
        HttpClientRequest httpClientRequest = new HttpClientRequest();
        HttpRequest.Builder builder = httpClientRequest.initBuilder("https://localhost:8443/v1/pets" + System.lineSeparator(), HttpMethod.GET);
        builder.setHeader("x-traceability-id", "1111111");
        HttpResponse<String> response = (HttpResponse<String>) httpClientRequest.send(builder, HttpResponse.BodyHandlers.ofString());
        // print status code
        System.out.println(response.statusCode());
        // print response body
        System.out.println(response.body());
    }

    /**
     * This is an example for post request. Please note that you need to set header TRANSFER_ENCODING
     * and pass the request body into the callback function.
     *
     * @throws Exception
     */
    public void testHttpPost() throws Exception {

    }

}
