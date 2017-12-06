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



### Check message on Kafka

The first step is to get into the container of kafka. First use docker ps to find
the container id for Kafka. And then use that container id to get into the container.

```
docker ps
docker exec -it 90472bd69381 bash
```


check how many topics in kafka


```
./bin/kafka-topics.sh --zookeeper zookeeper:2181 --list
```

check kafka topic for todo-list

```
./bin/kafka-console-consumer.sh --zookeeper zookeeper:2181 --topic com.networknt.eventuate.todolist.domain.TodoAggregate --from-beginning
```

