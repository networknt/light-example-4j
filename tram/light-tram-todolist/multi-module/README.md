# Todo List example application

It's challenging for micro-service API to update data (e.g. a Domain-Driven design aggregate) and
publish a message, such as a domain event.

The traditional approach of using 2PC/JTA isn't a good fit for micro-service applications.

The light-tram-4j framework implements an alternative mechanism based on the Application Events
pattern.

When an application creates or updates data, as part of that ACID transaction, it inserts an event
into an EVENTS or MESSAGES table. A separate process publishes those events to a message broker, such
as Apache Kafka.



## About the Todo list application

The Todo List application, which lets users maintain a todo list, is the end-to-end POC application
for the light-tram-4j framework.

It shows how use Eventuate Tram to:

  --reliably publish domain events as part of a database transaction that updates an aggregate.

  -- consume domain events to update a CQRS view

When a user creates or updates a todo, the application publishes a domain event. An event handler,
subscribes to those events and updates an ElasticSearch-based CQRS view.

## Todo list architecture


The Todo List application is built using

Java

light-4j

light-Tram-4j

Kafka

MySQL

ElasticSearch


The application persists the Todo entity in MySQL. It also maintains a materialized view of the data in
ElasticSearch.


## How it works


The Todo application uses the light-tram-4j framework to publish and consume domain events.

1. TodoCommandService persist todo entity to local data store and publishes an event when it creates, updates, or deletes a Todo. It uses the DomainEventPublisher, which is implemented by the light-tram-4j framework.

2. light-tram-4j cdc server will get the published events from MESSAGE/EVENT table and producer to Kafka message broker.

3. Todo application use TodoEventConsumer defines the event handlers, which update Elasticsearch.




## Building and running


Assume you created a working directory named networknt under user directory.


## Start Event-sourcing with mysql, zookeeper, kafka and cdc with mysql binlog;



## Setting DOCKER_HOST_IP for Mac

You can install Mysql, Kafka individually and start them at OS level and it is
the only option if you are using Windows. However, the most convenient way is to
use docker-compose to run the application services and eventuate infrastructure
services: Mysql, Zookeeper, Kafka and CDC server.

There is no special configuration for Linux as Docker is native. On Mac, Docker
still runs within a VM so you need to setup OS environment variable DOCKER_HOST_IP.

This variable sets the advertised listener of the Kafka container. It must be an
IP address (or a DNS name) that is accessible from both Docker containers and, if
you want to do development, from applications running on the host. Unfortunately,
because of version/platform-specific variations in how Docker works, setting this
variable is a little tricky.

Docker for Mac has [networking limitations](https://docs.docker.com/docker-for-mac/networking/)
and you need to follow the steps below to set it up.

```
sudo ifconfig lo0 alias 10.200.10.1/24  # (where 10.200.10.1 is some unused IP address)
export DOCKER_HOST_IP=10.200.10.1
```

Once you have complete the export command, please use the same terminal to start
docker-compose-eventuate.yml described in the next step as other terminal doesn't
have this DOCKER_HOST_IP set.



```
cd ~/networknt
git clone git@github.com:networknt/light-docker.git
cd ~/networknt/light-docker
docker-compose -f docker-compose-eventuate-local.yml up

```


Start light-tram-cdc service

```
cd ~/networknt/light-docker
docker-compose -f docker-compose-cdcserver-for-tram.yml up

```


## Checkout related projects and run service:

Two things should be verify before run the service:

1. since we are using Elasticsearch (CQRS opensouce), please increase the docker memory space to 4gb  for the docker.

    -- please refer issue: https://github.com/10up/wp-local-docker/issues/6

2. current default Elasticsearch host setting is for Mac, if you running the service for Linux, change it to "127.0.0.1" or the IP address running Elasticsearch

   - com.networknt.tram.todolist.view.TodoViewService:
     - com.networknt.tram.todolist.view.TodoViewServiceImpl:
       - java.lang.String: 10.200.10.1


```
cd ~/networknt
git clone git@github.com:networknt/light-example-4j.git
cd ~/networknt/light-example-4j/tram/light-tram-todolist/multi-module
mvn clean install
docker-compose up
```


Verify result:

1. Create Todo:

```
curl -X POST \
  http://localhost:8081/v1/todos \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -d '{"title":"canada","completed":false,"order":0}'

```

2. get todolist view (CQRS) based on the search value:

```
curl -X GET \
  'http://localhost:8082/v1/todoviews?searchValue=canada' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json'
```
