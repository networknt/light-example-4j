
# OpenAPI Light-4J Server


### Build and Start

The scaffolded project contains multiple modules. A fat jar server.jar will be generated in server/target directory after running the build command below.

```
./mvnw clean install
```

With the fatjar in the server/target directory, you can start the server with the following command.

```
java -jar server/target/server.jar
```




### Test

By default, the OAuth2 JWT security verification is disabled, so you can use Curl or Postman to test your service right after the server is started. For example, the petstore API has the following endpoint.

```
curl -k https://localhost:8443/v1/pets
```

For your API, you need to change the path to match your specifications.

### Tutorial

To explore more features, please visit the [petstore tutorial](https://doc.networknt.com/tutorial/rest/openapi/petstore/).