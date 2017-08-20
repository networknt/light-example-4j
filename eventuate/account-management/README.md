# Light-eventuate-4j example-- Account Money Transfer

Account Money Transfer example is built on light-4j, light-rest-4j and light-eventuate-4j which uses 
event sourcing and CQRS as major patterns to handle event communication between multiple microservices. 
It is a show case on event/message based communication between microservices instead of request/response
style.

The application consists of loosely coupled components that communicate using events and leverages
eventual consistency, event-driven approach rather than using traditional distributed transaction.

These components can be deployed either as separate services or packaged as a monolithic application 
for simplified development and testing.

# Structure of the example

Modules:

* common  

common module for the application, include DDD models and events for event sourcing.

* command  

command side common components, include command services

* query
  
query side common components, include query services

* e2etest

end to end test module


Here is the list of all services:

* Customers Service - REST API for creating customers
* Accounts Service - REST API for creating accounts
* Transactions Service - REST API for transferring money
* Customers View Service - subscribes to events and updates query side material view. And it provides an API for retrieving customers
* Accounts View Service - subscribes to events and updates query side material view. And it provides an API for retrieving accounts

# Building and running the microservices

Assume you created a working directory named networknt under user directory.

Checkout related projects.

```
cd ~/networknt
git clone git@github.com:networknt/light-example-4j.git
cd ~/networknt/light-example-4j/eventuate/account-management
mvn clean install
```

In order to start the example application which contains all the microserivces to handle business logic
we must first to start the light-eventuate-4j platform which provide event store and API for business
microserivces to publish and subscribe events. Also we need to start a separate CDC(change data capture) 
service to enable persisted events to be published to Kafka. 

Please follow this [tutorial](https://networknt.github.io/light-eventuate-4j/tutorial/service-dev/) 
on how to start event store and cdc server. 

# Steps to start account management services

```
cd ~/networknt/light-example-4j/eventuate/account-management
docker-compose up
```

# Test to verify result:

Create a new customer:

```
  curl -X POST \
    http://localhost:8083/v1/createcustomer \
    -H 'cache-control: no-cache' \
    -H 'content-type: application/json' \
    -d '{"name":{"firstName":"Google11","lastName":"Com"},"email":"aaa1.bbb1@google.com","password":"password","ssn":"9999999999","phoneNumber":"4166666666","address":{"street1":"Yonge St","street2":"2556 unit","city":"toronto","state":"ON","zipCode":"Canada","country":"L3R 5F5"}}'
```

View the new customer:

```
curl http://localhost:8084/v1/customers/aaa1.bbb1@google.com
```

For more information on how to use the application, please refer to the [tutorial](https://networknt.github.io/light-eventuate-4j/example/transfer_integration/).
