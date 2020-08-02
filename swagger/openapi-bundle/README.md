# OpenAPI bunldle example

This example project used to show how to use the maven plugin to bunlde openapi files and generate a final openapi specification.


## Introduction:

 For microservice application, normally the API contract (openapi specification ) won't be too big, it will be fine to define in a yml file. But in some cases, the openapi specification would use
 lots to object model in schemas. And for organizations, they would like have the APIs share the domain object model, and will put the  domain object model in separate yml files for sharing. In these cases, we may need define the openapi spec by the ref to the files"



```
            application/json:
              schema:
                $ref: './schemas/error.yaml#/Error'


```

Then we will need generate the 'final' openapi specification for light-codegen and schema validation.

The example project shows have to use the maven plugin and bundle the yml files and generate final openapi specification.



## Modules

 -- specification

   there is an openapi.yml file which define the schema ref to the ymls files under ./schemas folder



## Build and verify


 ```
 cd ~/networknt
 git clone git@github.com:networknt/light-example-4j.git
 cd ~/networknt/light-example-4j/swagger/openapi-bundle

 mvn clean install


 ```

 The Final openapi specification will be generated to specified folder (it define on pom file ).

  ```
<output>${project.basedir}/target</output>

  ```

And we can implement the openapi bundle together with light-codegen maven plugin together. For the light-codegen with maven build, please refer to:

https://github.com/networknt/light-example-4j/tree/1.6.x/servicemesher/services

