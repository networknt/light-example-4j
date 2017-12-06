This is a demo application that for light-eventuate-4j to use Event Sourcing and
CQRS with Kafka as messaging broker. 

### Build

```
mvn clean package

```

### Restful serivce



To create a todo item.

```
curl -k -H "Content-Type: application/json" -X POST   https://localhost:8081/v1/todos   -d '{"title":"buy grocery","completed":false,"order":1}'
```


### Hybrid services

