# Light-4j service example with light-codegen

## Modules

-- petstore-spec

  The module contains service specification. In the example, we are using openapi-3.0 spec.

  And the config.json is the setting config file for light-codegen to generate service project

  ```
  {
    "name": "petstore",
    "version": "3.0.1",
    "groupId": "com.networknt",
    "artifactId": "petstore",
    "rootPackage": "com.networknt.petstore",
    "handlerPackage":"com.networknt.petstore.handler",
    "modelPackage":"com.networknt.petstore.model",
    "overwriteHandler": true,
    "overwriteHandlerTest": true,
    "overwriteModel": true,
    "httpPort": 8080,
    "enableHttp": false,
    "httpsPort": 8443,
    "enableHttps": true,
    "enableHttp2": true,
    "enableRegistry": false,
    "supportDb": false,
    "supportH2ForTest": false,
    "supportClient": false,
    "specChangeCodeReGenOnly": true,
    "dockerOrganization": "networknt"
  }


  ```


-- petstore-service

  The microservice API module.


## Project build process



 In the first time build project, copy the openapi spec to the folder /petstore/config, and set the config.json config value specChangeCodeReGenOnly as false

  ```
    "specChangeCodeReGenOnly": false,

  ```

 run maven build from root folder:

   ```
      cd ~/networknt/light-example-4j/rest/perstore-with-codegen

      mvn clean install

   ```

 The command will call execute light-codegen command by maven plugin setting to initial generate petstore-service api. After develop started ti developer the service, change specChangeCodeReGenOnly as true;
 then the new code generation will only generate code for specification change, for example, new endpoint, new data module.. etc


 The following is the maven plugin setting for executing light-codegen:

    ```
             <plugin>
                 <groupId>org.codehaus.mojo</groupId>
                 <artifactId>exec-maven-plugin</artifactId>
                 <version>1.4.0</version>
                 <executions>
                     <execution>
                         <id>light4j-codegen</id>
                         <phase>process-resources</phase>
                         <goals>
                             <goal>exec</goal>
                         </goals>
                         <inherited>false</inherited>
                         <configuration>
                             <executable>java</executable>
                             <arguments>
                                 <argument>-jar</argument>
                                 <argument>${settings.localRepository}/com/networknt/codegen-cli/${version.light-4j}/codegen-cli-${version.light-4j}.jar</argument>
                                 <argument>-f</argument>
                                 <argument>openapi</argument>
                                 <argument>-o</argument>
                                 <argument>../petstore-service</argument>
                                 <argument>-m</argument>
                                 <argument>config/openapi.yaml</argument>
                                 <argument>-c</argument>
                                 <argument>config/config.json</argument>
                             </arguments>
                         </configuration>
                     </execution>
                 </executions>

             </plugin>

    ```