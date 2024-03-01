This folder is created to reproduce the issue reported by @logi.

https://github.com/networknt/light-router/issues/22

Also, it serve as a demo for how to create a backend post-service, a frontend post-client and configuration for the light-router sitting in between. You can learn how to build a standalone client to interact with a server or a router.

### post-service

The post-service is very simple with only one endpoint /v1/postData and you can switch between HTTP and HTTPS&HTTP2 in the server.yml in src/main/resources/config folder.

To start the server, go to the post-service folder

```
mvn clean install exec:exec
```

If you server is listening to HTTPS, then you can use the following curl command to access it.

```
curl -k -X POST https://localhost:8443/v1/postData -H 'content-type: application/json' -H 'host: localhost' -d '[{"key": "key", "value": "value"}, {"key": "key", "value": "value"}]'
```

The response should be the same like the request body.

```
[{"key": "key", "value": "value"}, {"key": "key", "value": "value"}]
```

If you server is listening to HTTP, then you can use the following curl command to access it.

```
curl -X POST http://localhost:8080/v1/postData -H 'content-type: application/json' -H 'host: localhost' -d '[{"key": "key", "value": "value"}, {"key": "key", "value": "value"}]'
```

### post-client

So we have the backend post-service up and running and we can confirm it is working with one of the commands above. Now, let's create a standalone Java client to access the service with Http2Client.

The client is very simple with a main method to call different combinations of tests.

```
    public static void main(String[] args) throws Exception {
        Http2ClientExample e = new Http2ClientExample();
        //e.testWrongPath();
        //e.testDirect4k();
        //e.testDirect48k();
        //e.testRouterHttps4k();
        e.testRouterHttps48k();
        //e.testProxy4k();
        //e.testProxy48k();
        System.exit(0);
    }
```

The issue we have with light-router reported by @logi is that if the post body is bigger than 24k, the post-service only receives partial of the body and it fails to parse it in the BodyHandler.

So I have created several test and you can call them one by one in the main method.

* Direct4k and Direct48k are using https/http2 to connect to the post-service directly.
* RouterHttps4k and RouterHttps48k are using light-router in between client and service.
* Proxy4k and Proxy48k are using the default undertow reverse proxy with http only connection.

Basically, it load a file from resources folder and send to the service as request body. Here is one example.

```
    public void testRouterHttps48k() throws Exception {
        ClientConnection connection = null;
        try {
            connection = client.connect(new URI("https://localhost:8000"), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true)).get();
            final String json = getResourceFileAsString("48k.json");
            for(int i = 0; i < 100; i++) {
                final CountDownLatch latch = new CountDownLatch(1);
                final AtomicReference<ClientResponse> reference = new AtomicReference<>();
                final ClientRequest request = new ClientRequest().setMethod(Methods.POST).setPath("/v1/postData");
                request.getRequestHeaders().put(Headers.HOST, "localhost");
                request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
                request.getRequestHeaders().put(Headers.CONTENT_TYPE, "application/json");
                connection.sendRequest(request, client.createClientCallback(reference, latch, json));
                latch.await(1000, TimeUnit.MILLISECONDS);
                int statusCode = reference.get().getResponseCode();
                String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
                if(statusCode != 200) System.out.println("testRouter4k failed with statusCode = " + statusCode + " body = " + body);
                System.out.println(i);
                if(!body.startsWith("[")) System.out.println(body);
            }
        } finally {
            if(connection != null) IoUtils.safeClose(connection);
        }
    }

```

You can see we are using the same connection to send 100 requests and then close it at the end.


### post-router

To make things simple, we are not going to use service registry and discovery with Consul here in light-router. Instead, we are going to use direct registry for service lookup.

There is a docker-compose.yml in the root folder of post-router and there are two sub folders that contain https and http configurations.

```
version: '2'

services:

  light-router:
    image:  networknt/light-router:latest
    networks:
    - localnet
    ports:
    - 8080:8080
    volumes:
    - ./https:/config
    # - ./http:/config

#
# Networks
#
networks:
    localnet:
        # driver: bridge
        external: true

```

You can switch between https and http in the volumes in the docker-compose.yml file above. https config folder contains configuration to support both HTTPS and HTTP/2 for incoming and outgoing requests.

In order to make it simple, I have plugged in the PathPrefixService middleware handler to map the path /v1/postData to the serviceId com.networknt.postservice-1.0.0 to assist service discovery.

```
# indicate if PathPrefixServiceHandler is enabled or not
enabled: true
# mapping from request path prefixes to serviceIds
mapping:
  /v1/postData: com.networknt.postservice-1.0.0

```

To call the router from command line with curl.

```
curl -k -X POST https://localhost:8000/v1/postData -H 'content-type: application/json' -H 'host: localhost' -d '[{"key": "key", "value": "value"}, {"key": "key", "value": "value"}]'
```

With all the preparation above, I have successfully reproduced the issue. By debugging with three IDEs on client, router and service, I found out that the issue was caused by the Http2Client BufferPool buffer size. It was default to 24K which should be OK for most of the applications. But when passing bigger requests, it is not enough. The solution is to make it configurable in the client.yml file in client module.


```
# This is the configuration file for Http2Client.
---
# Buffer Size in the buffer pool in KB. If should be bigger than your request or response body size.
bufferSize: 80
# Settings for TLS
tls:
  # if the server is using self-signed certificate, this need to be false. If true, you have to use CA signed certificate
  # or load truststore that contains the self-signed cretificate.
  verifyHostname: true
  # trust store contains certifictes that server needs. Enable if tls is used.
  loadTrustStore: true
  # trust store location can be specified here or system properties javax.net.ssl.trustStore and password javax.net.ssl.trustStorePassword
  trustStore: client.truststore
  # key store contains client key and it should be loaded if two-way ssl is uesed.
  loadKeyStore: false
  # key store location
  keyStore: client.keystore
# settings for OAuth2 server communication
oauth:
  # OAuth 2.0 token endpoint configuration
  token:
    # The scope token will be renewed automatically 1 minutes before expiry
    tokenRenewBeforeExpired: 60000
    # if scope token is expired, we need short delay so that we can retry faster.
    expiredRefreshRetryDelay: 2000
    # if scope token is not expired but in renew windown, we need slow retry delay.
    earlyRefreshRetryDelay: 4000
    # token server url. The default port number for token service is 6882.
    server_url: https://localhost:6882
    # token service unique id for OAuth 2.0 provider
    serviceId: com.networknt.oauth2-token-1.0.0
    # set to true if the oauth2 provider supports HTTP/2
    enableHttp2: true
    # the following section defines uri and parameters for authorization code grant type
    authorization_code:
      # token endpoint for authorization code grant
      uri: "/oauth2/token"
      # client_id for authorization code grant flow. client_secret is in secret.yml
      client_id: f7d42348-c647-4efb-a52d-4c5787421e72
      # the web server uri that will receive the redirected authorization code
      redirect_uri: https://localhost:8080/authorization_code
      # optional scope, default scope in the client registration will be used if not defined.
      scope:
      - petstore.r
      - petstore.w
    # the following section defines uri and parameters for client credentials grant type
    client_credentials:
      # token endpoint for client credentials grant
      uri: "/oauth2/token"
      # client_id for client credentials grant flow. client_secret is in secret.yml
      client_id: f7d42348-c647-4efb-a52d-4c5787421e72
      # optional scope, default scope in the client registration will be used if not defined.
      scope:
      - petstore.r
      - petstore.w
    refresh_token:
      # token endpoint for refresh token grant
      uri: "/oauth2/token"
      # client_id for refresh token grant flow. client_secret is in secret.yml
      client_id: f7d42348-c647-4efb-a52d-4c5787421e72
      # optional scope, default scope in the client registration will be used if not defined.
      scope:
      - petstore.r
      - petstore.w
  # light-oauth2 key distribution endpoint configuration
  key:
    # key distribution server url
    server_url: https://localhost:6886
    # the unique service id for key distribution service
    serviceId: com.networknt.oauth2-key-1.0.0
    # the path for the key distribution endpoint
    uri: "/oauth2/key"
    # client_id used to access key distribution service. It can be the same client_id with token service or not.
    client_id: f7d42348-c647-4efb-a52d-4c5787421e72
    # set to true if the oauth2 provider supports HTTP/2
    enableHttp2: true
  # de-ref by reference token to JWT token. It is separate service as it might be the external OAuth 2.0 provider.
  deref:
    # Token service server url, this might be different than the above token server url.
    server_url: https://localhost:6882
    # token service unique id for OAuth 2.0 provider. Need for service lookup/discovery.
    serviceId: com.networknt.oauth2-token-1.0.0
    # set to true if the oauth2 provider supports HTTP/2
    enableHttp2: true
    # the path for the key distribution endpoint
    uri: "/oauth2/deref"
    # client_id used to access key distribution service. It can be the same client_id with token service or not.
    client_id: f7d42348-c647-4efb-a52d-4c5787421e72
```

As you can see the above client.yml has bufferSize 80 which mean 80*1024. It should be bigger enough to handler 48K request body. If the bufferSize is not in the client.yml, then the default would be 24*1024.

There are two places that Http2Client is used in this setup.

* The post-client has a client.yml that needs to be updated to set new bufferSize.
* The post-router has a client.yml that needs to be updated to set new bufferSize.

After the bufferSize is updated, we still see failure on the post-client when using 48K reqeust body. It was due to the wait time too short at 100ms. After increase it to 1000ms everything works perfect.

```
latch.await(1000, TimeUnit.MILLISECONDS);
```

### Summary

When using the client module, you need to gauge the application and set the bufferSize for the buffer pool in client.yml config file. Also, if you are expecting too many request sent to the server, you need to adjust the latch.await timeout a little longer.

The entire execise to debug this issue is a good process and it would help other developers to learn how to debug the distributed application which involves several independent processes. I have recorded a video and the link is below. Hope this helps others.

https://youtu.be/qW0xIC_A_KI
