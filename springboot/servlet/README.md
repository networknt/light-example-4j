This is an example to demo how to inject light-4j middleware handlers to address cross-cutting concerns in a Spring Boot application with embedded Undertow Servlet Web Server. It is based on the OpenAPI 3.0 petstore specification with some modifications to allow users to see two different implementations for the endpoints. 

### Specification

Light-4j encourages design-driven approach and it can load the OpenAPI 3.0 specification during the runtime to validate the request and verify the JWT token scopes against the specification. For this example application, the specification can be found at [model-config](https://github.com/networknt/model-config/tree/master/rest/springboot/servlet) repo. 

As you can see, for every funcation, we have both Spring Boot and Light-4j implementations with path prefix with `/spring` or `/light`.

### Start Server

```
git clone https://github.com/networknt/light-example-4j.git
cd light-example-4j/springboot/servlet
./mvnw clean install spring-boot:run
```

### Test

We are using curl to test the restful services. 

##### To get a list of pets:

Light-4j endpoint.

```
curl http://localhost:8080/light/pets
```

Spring Boot endpoint.

```
curl http://localhost:8080/spring/pets
```

Both commands will return the same result and the light-4j middleware handlers are all executed. 

```
[{"id":1,"name":"catten","tag":"cat"},{"id":2,"name":"doggy","tag":"dog"}]
```


##### To get a list of pets with limit

Light-4j endpoint.

```
curl http://localhost:8080/light/pets?limit=10
```

Spring Boot endpoint.

```
curl http://localhost:8080/spring/pets?limit=10
```

Both commands will return the same result and the light-4j middleware handlers are all executed. 

```
[{"id":1,"name":"catten","tag":"cat"},{"id":2,"name":"doggy","tag":"dog"}]
```


##### To get a list of pets with incorrect limit type

Light-4j endpoint.

```
curl http://localhost:8080/light/pets?limit=abc
```

Spring Boot endpoint.

```
curl http://localhost:8080/spring/pets?limit=abc
```

In the specification, we have defined the limit as integer. When `abc` is used, both commands will return the same error as light-4j [openapi-validator](https://doc.networknt.com/style/light-rest-4j/openapi-validator/) is injected at low level http core. It invokes [json-schema-validator](https://github.com/networknt/json-schema-validator) for the query parameter limit.

```
{"statusCode":400,"code":"ERR11004","message":"VALIDATOR_SCHEMA","description":"Schema Validation Error - $: string found, integer expected","severity":"ERROR"}
```

##### To get a single pet by Id

Light-4j endpoint.

```
curl http://localhost:8080/light/pets/111
```

Spring Boot endpoint.

```
curl http://localhost:8080/spring/pets/111
```

Both commands will return the same result and the light-4j middleware handlers are all executed. 

```
{"id":1,"name":"Jessica Right","tag":"pet"}
```

##### To create a new pet with Post request

Light-4j endpoint.

```
curl -X POST http://localhost:8080/light/pets -H 'Content-Type: application/json' -d '{"id":1,"name":"Jessica Right","tag":"pet"}'
```

Spring Boot endpoint.

```
curl -X POST http://localhost:8080/spring/pets -H 'Content-Type: application/json' -d '{"id":1,"name":"Jessica Right","tag":"pet"}'
```

Both commands will return the same result and the light-4j middleware handlers are all executed. 

```
{"id":1,"name":"Jessica Right","tag":"pet"}
```

Note that the body validation is disabled in the openapi-validator.yml as we have to pass the body stream to the Spring Boot. 


##### To delete a pet


Light-4j endpoint.

```
curl -X DELETE http://localhost:8080/light/pets/1 -H 'key: 1'
```

Spring Boot endpoint.

```
curl -X DELETE http://localhost:8080/spring/pets/1 -H 'key: 1'
```

Both commands will return the same result and the light-4j middleware handlers are all executed. 

```
{"id":1,"name":"Jessica Right","tag":"pet"}
```

Note that there is an extra header `key: 1` in the request as the specification for this endpoint require a key header. You can try to remove the key header and see the validation error as below. 

```
{"statusCode":400,"code":"ERR11017","message":"VALIDATOR_REQUEST_PARAMETER_HEADER_MISSING","description":"Header parameter key is required on path /light/pets/{petId} but not found in request.","severity":"ERROR"}
```

