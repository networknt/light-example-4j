This is a demo of Http2Client connecting to Sprint Boot with embedded Tomcat. 

To start the server, please do the following.

```
git clone git@github.com:networknt/microservices-framework-benchmark.git
cd microservices-framework-benchmark
cd spring-boot-tomcat
mvn clean install
mvn spring-boot:run
```

Now the server is up and running and you can access it through curl.

```
curl localhost:8080
```
