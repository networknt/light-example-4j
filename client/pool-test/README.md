This is a client using light-4j Http2Client to test the connection pool with HTTPS/HTTP2. We need to make sure that the connection will be expired in 90s and move it into the parked pool so that it can be closed later. We need to make sure that no connection is left in the connection pool.

To test this live, please start the http server petstore with the following commands.

This consumer application is converted to 2.1.10 light-4j and it is used to test and debug the issue https://github.com/networknt/light-4j/issues/1699

```
cd ~/networknt
git clone git@github.com:networknt/light-example-4j.git
cd rest/petstore-maven-single
mvn clean install -Prelease
java -jar target/server.jar
```
