# Light Hybrid 4J Server

## Start server

If you only have one service jar file, then your can include the jar file into the
class path as below.

```
java -cp petstore-1.0.1.jar:target/petstore-1.0.1.jar com.networknt.server.Server
```

If you have multiple service jar files, you'd better create a directory and include
that directory into the classpath when starting the server.

```
java -cp petstore-1.0.1.jar:/service/* com.networknt.server.Server
```


## Test
