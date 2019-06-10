### Introduction

This is a low-level routing implementation on top of light-4j for maximum performance and flexibility. It is a bare-bones implementation of light-4j service, and it is recommended only for advanced users. For most users, it is recommended to user light-codegen to scaffold a new project based on the specification like Swagger 2.0, OpenAPI 3.0, GraphQL schema, and Hybrid schema. With light-codegen, common cross-cutting concerns (middleware handlers) are wired in by default. 

This example implementation of light-4j is to demo different URI pattern matching with different verbs.

### Getting Started

You can build and start the service with the following Maven command. 

```
mvn clean install exec:exec
```

Or you can build it first and start it with the `java -jar` command line.

```
mvn clean install
java -jar target/routing-0.1.1-SNAPSHOT.jar
```

The server will be started and listen to the https port 8443.

### Test

To test the server, issue the following command from another terminal.

```
curl -k https://localhost:8443/baz
```

You can try other HTTP method and path defined in the handler with curl or postman. 



