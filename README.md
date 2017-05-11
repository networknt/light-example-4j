# light-example-4j
Example APIs to demo all feature of the light-4j and frameworks built on top of light-4j.

## light-rest-4j

This is a RESTful API framework which works with OpenAPI specification. The examples are located
in rest folder.

#### petstore

This is a generic service and it is used to test new release and new generator. The following
command line will generate the petstore again with new version of light-codegen

```
cd ~/networknt/light-codegen/codegen-cli
java -jar target/codegen-cli.jar -f light-rest-4j -o ~/networknt/light-example-4j/rest/petstore -m ~/networknt/model-config/rest/petstore/swagger.json -c ~/networknt/model-config/rest/petstore/config.json
```

## light-hybrid-4j

This is a framework support both Monolithic and Microservice at the same time and taking
advantages on both sides. The examples are located in hybrid folder.

#### template server

This is a generic server that can host many light-hybrid-4j services. The following command line
will generate this project when a new version of framework is released or the templates are changed.
Note that you need to recompile the entire project and regenerate the example when templates are changed.

```
java -jar target/codegen-cli.jar -f light-hybrid-4j-server -o ~/networknt/light-example-4j/hybrid/server -c ~/networknt/light-codegen/light-hybrid-4j/src/test/resources/serverConfig.json
```

#### template service

This is a generic service that delivers a small jar file that can be hosted on light-hybrid-4j-server.
The following command line can generate this service when framework is upgraded or templates are 
Changed. 

```
java -jar target/codegen-cli.jar -f light-hybrid-4j-service -o ~/networknt/light-example-4j/hybrid/service -m ~/networknt/light-codegen/light-hybrid-4j/src/test/resources/schema.json -c ~/networknt/light-codegen/light-hybrid-4j/src/test/resources/serviceConfig.json
```

Now you can deploy the service to server platform and test it.

```
cd ~/networknt/light-example-4j/hybrid/service
mvn clean install
cd ..
cd server
mvn clean install
cp ../service/target/petstore-1.0.1.jar .
java -cp petstore-1.0.1.jar:target/petstore-1.0.1.jar com.networknt.server.Server
```

Now issue the following curl command to test the service.

```
curl -X POST \
  http://localhost:8080/api/json \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 58bb63eb-de70-b855-a633-5b043bb52c95' \
  -d '{
  "host": "lightapi.net",
  "service": "world",
  "action": "hello",
  "version": "0.1.1",
  "lastName": "Hu",
  "firstName": "Steve"
}'
```


