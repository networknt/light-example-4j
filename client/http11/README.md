This is the client to test Http2Client to connect to HTTPS/1.1 server. We have an issue report from one of our customers to connect to a legacy server.

Here we will use petstore server started with https and http1/1. For the Http2Client, we will try to set it up to support both HTTP2/HTTPS and to see if it can downgrade the connection to HTTP/1.1 gracefully regardless the configuration option.

Here is the steps to start the server assuming you have the light-example-4j checked out in networknt folder under your home directory.


```
cd ~/networknt/light-example-4j/rest/petstore-maven-single
mvn clean install -Prelease
SERVER_ENABLEHTTP2=false java -jar target/server.jar

```

You can confirm that the petstore server is started with HTTP/1.1 with /server/info
