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

