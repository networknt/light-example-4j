This is a demo application for mongodb. It uses the same swagger spec as database example which
is focus on SQL database only. 

```
java -jar modules/swagger-codegen-cli/target/swagger-codegen-cli.jar generate -i ~/networknt/swagger/database/swagger.json -c ~/networknt/swagger/database/config.json -l light-java -o ~/networknt/light-java-example/mongodb
```