# Light-eventuate-4j example-- Account Money Transfer

Account Money Transfer example is built on light-4j, light-rest-4j and light-eventuate-4j which uses 
event sourcing and CQRS as major patterns to handle event process between multiple microservices,

The application consists of loosely coupled components that communicate using events and leverages
eventual consistency, event-driven approach rather than using tranditional distributed transaction.

These components can be deployed either as separate services or packaged as a monolithic application 
for simplified development and testing.

# Structure of the example

Modules:

* common  

common module for the application, include DDD models and events for event sourcing.

* command  

command side common components, include command, services

* query
  
query side common components, include command, services

* e2etest

end to end test module


Here is the list of all services:

* Customers Service - REST API for creating customers
* Accounts Service - REST API for creating accounts
* Transactions Service - REST API for transferring money
* Customers View Service - subscribes to events and updates query side material view. And it provides an API for retrieving customers
* Accounts View Service - subscribes to events and updates query side material view, And it provides an API for retrieving accounts

# Building and running the microservices

Assume you created a working directory named networknt under user directory.

Checkout related projects.


Get the example project from github:

```
cd ~/networknt
git clone git@github.com:networknt/light-eventuate-example.git
cd ~/networknt/light-eventuate-example/account-management
mvn clean install
```


# Steps to start event-store and microservices


1. Go to light-eventuate-4j root folder:

    -- cd cd ~/networknt/light-eventuate-4j

2. Run docker compose file to start event store and CDC service:

    -- docker-compose up
    -- run docker-compose -f docker-compose-cdcservice.yml up

3. Go to account money transfer example root folder:

    -- cd ~/networknt/light-eventuate-example/account-management

4.  Run docker compose to start microservices

   -- docker-compose up



# Test to verify result:

Create a new customer:

  curl -X POST \
    http://localhost:8083/v1/createcustomer \
    -H 'cache-control: no-cache' \
    -H 'content-type: application/json' \
    -d '{"name":{"firstName":"Google11","lastName":"Com"},"email":"aaa1.bbb1@google.com","password":"password","ssn":"9999999999","phoneNumber":"4166666666","address":{"street1":"Yonge St","street2":"2556 unit","city":"toronto","state":"ON","zipCode":"Canada","country":"L3R 5F5"}}'

View the new customer:

http://localhost:8084/v1/customers/aaa1.bbb1@google.com


