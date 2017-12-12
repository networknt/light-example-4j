# Eventuate sourcing example-- Account Money Transfer

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
cd ~/workspace
git clone git@github.com:networknt/light-example-4j.git
cd ~/workspace/light-example-4j/eventuate/account-management
mvn clean install
```

In order to start the example application which contains all the microserivces to handle business logic
we must first to start the light-eventuate-4j platform which provide event store and API for business
microserivces to publish and subscribe events. Also we need to start a separate CDC(change data capture)
service to enable persisted events to be published to Kafka.

## Event-sourcing with mysql, zookeeper, kafka and cdc with mysql binlog

Please follow this [tutorial](https://networknt.github.io/light-eventuate-4j/tutorial/service-dev/)
on how to start event store and cdc server.


## Event-sourcing with oracle, zookeeper, kafka and cdc with oracle db polling

To start event sourcing with Oracle-Kafka, please go to networknt/light-docker project to start oracle-kafka event-sourcing by docker compose:


```
cd ~/workspace
git clone git@github.com:networknt/light-docker.git
cd ~/workspace/light-docker
docker-compose -f docker-compose-oracle.yml up

```

Then open another console window to start oracle polling cdc server

```
cd ~/workspace
cd ~/workspace/light-docker
docker-compose -f docker-compose-cdcserver-local-oracle-polling.yml up

```



# Steps to start account management services

## start service with Event-sourcing with mysql, zookeeper, kafka and cdc with mysql binlog

```
cd ~/networknt/light-example-4j/eventuate/account-management
docker-compose up
```


## start service with Event-sourcing with oracle, zookeeper, kafka and cdc with oracle db polling

```
cd ~/networknt/light-example-4j/eventuate/account-management
docker-compose -f docker-compose-oracle-event-sourcing up
```



# Test and verify result:


## From command line:

## Create new customer (C1):

* On customer command side, system sent the CreateCustomerCommand and apply CustomerCreatedEvent event.
* On customer view side, system subscribe the CustomerCreatedEvent by registered event handlers. In the example, system will process event and save customer to local database.


```
curl -X POST \
  http://localhost:8083/v1/createcustomer \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{"name":{"firstName":"Google11","lastName":"Com"},"email":"aaa1.bbb1@google.com","password":"password","ssn":"9999999999","phoneNumber":"4166666666","address":{"street1":"Yonge St","street2":"2556 unit","city":"toronto","state":"ON","zipCode":"Canada","country":"L3R 5F5"}}'
```

Sample Result:

```
{"id":"0000015cf50351d8-0242ac1200060000","customerInfo":{"name":{"firstName":"Google22","lastName":"Com"},"email":"aaa1.bbb@google.com","password":"password","ssn":"9999999999","phoneNumber":"4166666666","address":{"street1":"Yonge St","street2":"2556 unit","city":"toronto","state":"ON","zipCode":"Canada","country":"L3R 5F5"}}}
```

The json format result include system generated customer id for new created customer: "id":"0000015cf50351d8-0242ac1200060000". Get the customer id (C1) from your real result and save it in a text file.  We could use the customer id in the open account restful API call to open account the customer.


## Create an account with the customer (replace the customer id with real customer id):

* On account command side, system sent the OpenAccountCommand and apply AccountOpenedEvent event.
* On account view side, system subscribe the  AccountOpenedEvent by registered event handlers. In the example, system will process event and save account and account/customer relationship to local database.
* Open account for the customer created above, use the customer id above in the json data for HTTP POST: customerId":"0000015cf50351d8-0242ac1200060000"

```
curl -X POST \
  http://localhost:8081/v1/openaccount \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{"customerId":"0000015cf50351d8-0242ac1200060000","title":"RRSP account","description":"account for testing","initialBalance":12355}'
```

Result:

```
{"accountId":"0000015cf505ed48-0242ac1200090001","balance":12355}
```

The json format result include system generated account id for new opened account: "accountId":"0000015cf505ed48-0242ac1200090001". Get the account id (A1) from your real result and save it in a text file. We could use the account Id for money transfer restful API.



## Create an account (no link with customer)

* On account command side, system sent the OpenAccountCommand and apply AccountOpenedEvent event.
* On account view side, system subscribe the AccountOpenedEvent by registered event handlers. In the example, system will process event and save account to local database.

```
curl -X POST \
  http://localhost:8081/v1/openaccount \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{"title":"RRSP account","description":"account for testing","initialBalance":12355}'
```

Result:

```
{"accountId":"0000015cf5084627-0242ac1200090001","balance":12355}
```

The json format result include system generated account id for new opened account: "accountId":"0000015cf5084627-0242ac1200090001". Get the account id (A2) from your real result and save it in a text file. It will be used to link with customer (C2) in "Link account to customer" service call.


## Create new customer (C2):

* On customer command side, system sent the CreateCustomerCommand and apply CustomerCreatedEvent event.
* On customer view side, system subscribe the CustomerCreatedEvent by registered event handlers. In the example, system will process event and save customer to local database.

```
curl -X POST \
  http://localhost:8083/v1/createcustomer \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{"name":{"firstName":"Google11","lastName":"Com"},"email":"aaa2.bbb@google.com","password":"password","ssn":"9999999999","phoneNumber":"4166666666","address":{"street1":"Yonge St","street2":"2556 unit","city":"toronto","state":"ON","zipCode":"Canada","country":"L3R 5F5"}}'
```

Result:
```
{"id":"0000015cf50bfe50-0242ac1200060001","customerInfo":{"name":{"firstName":"Google11","lastName":"Com"},"email":"aaa2.bbb@google.com","password":"password","ssn":"9999999999","phoneNumber":"4166666666","address":{"street1":"Yonge St","street2":"2556 unit","city":"toronto","state":"ON","zipCode":"Canada","country":"L3R 5F5"}}}
```

The json format result include system generated customer id for new created customer: "id":"0000015cf50bfe50-0242ac1200060001". Get the customer id (C2) from your real result and save it in a text file.  It will be used to link with account (opened on previous service call) in "Link account to customer" service call.



## Link account to customer (replace the customer id and account with real Id):

* On customer command side, system sent the AddToAccountCommand and apply CustomerAddedToAccount event.
* On customer view side, system subscribe the CustomerAddedToAccount event by registered event handlers. In the example, system will process event and save customer/account relationship to local database.
* Use the new created customer id (replace the {customerId}  with customer id (C2) in the url) and replace tge account id (A2) in POST json data which created on previous service call.

```
curl -X POST \
  http://localhost:8083/v1/customers/toaccounts/{customerId} \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{"id":"0000015cf5084627-0242ac1200090001","title":"title","owner":"google","description":"test case"}'
```

Result: 0000015cf50bfe50-0242ac1200060001



## Transfer money from account (replace the from account and to account id with real id):

* On transaction command side, system send MoneyTransferCommand and apply the MoneyTransferCreatedEvent event with certain amount
* On account command side, system subscribes the MoneyTransferCreatedEvent event. System will verify the account balance based on the debit event.
* If the balance is not enough, system publish AccountDebitFailedDueToInsufficientFundsEvent. Otherwise, system send AccountCreditedEvent/AccountDebitedEvent.
* On transaction command side, if subscribed events are AccountCreditedEvent/AccountDebitedEvent, system will process event and publish CreditRecordedEvent/debitRecordedevent,
  if subscribed event is AccountDebitFailedDueToInsufficientFundsEvent, system will publish FailedDebitRecordedEvent.
* On account view side, if subscribed events are creditRecorded event and debitRecorded event, system will update local account balance and update the transaction status to COMPLETED.
  if subscribed even FailedDebitRecordedEvent, system will update transaction status to FAILED_DUE_TO_INSUFFICIENT_FUNDS.
* Use the two new opened account id (A1, A2) on previous services for the fromAccountId and toAccountId in the following service call.

```
curl -X POST \
  http://localhost:8085/v1/transfers \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{"fromAccountId":"0000015cf505ed48-0242ac1200090001","toAccountId":"0000015cf5084627-0242ac1200090001","amount":5000,"description":"test"}'
```

Result:

```
{"moneyTransferId":"0000015cf5118e64-0242ac1200080000"}
```

Return system generated money transfer transaction id


## Delete account:

* On account command side, system sent the DeleteAccountCommand  and apply AccountDeletedEvent event.
* On account view side, system subscrible the  AccountDeletedEvent by registered event handles. On the example, system will process event and inactive account to local database.
* On customer view side, system subscrible the  AccountDeletedEvent event by registered event handles. On the example, system will process event and delete customer/account relationship to local database.

```
curl -X DELETE \
  http://localhost:8081/v1/delete/0000015cf4bec29b-0242ac1200070001 \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
```



## Customer and Account view:

--View the new customer by email (provide wildcard search):

```
curl http://localhost:8084/v1/customers/aaa1.bbb1@google.com
```

Result:

```
{"customers":[{"id":"0000015cf50351d8-0242ac1200060000","name":{"firstName":"Google22","lastName":"Com"},"email":"aaa1.bbb@google.com","password":"password","ssn":"9999999999","phoneNumber":"4166666666","address":{"street1":"Yonge St","street2":"2556 unit","city":"toronto","state":"ON","zipCode":"Canada","country":"L3R 5F5"},"toAccounts":null}]}
```


--View the new customer by customer Id (replace the customer id with real customer id,. You can use copy from result of create customer)

```
curl http://localhost:8084/v1/customer/{customerId}
```

Result:

```
{"id":"0000015cf50351d8-0242ac1200060000","name":{"firstName":"Google22","lastName":"Com"},"email":"aaa1.bbb@google.com","password":"password","ssn":"9999999999","phoneNumber":"4166666666","address":{"street1":"Yonge St","street2":"2556 unit","city":"toronto","state":"ON","zipCode":"Canada","country":"L3R 5F5"},"toAccounts":null}
```

-- view account by Id (replace the Id with real account Id)

```
curl http://localhost:8082/v1/accounts/{accountId}
```

Result:

```
{"accountId":"0000015cf505ed48-0242ac1200090001","balance":73550000,"title":"RRSP account","description":"account for testing"}
```

-- View account transaction history:

```
curl http://localhost:8082/v1/accounts/0000015cf910e31b-0242ac1200080000/history
```

Result:

```
[{"date":"2017-06-30","entryType":null,"transactionId":"0000015cf914b22e-0242ac1200070001","fromAccountId":"0000015cf910e31b-0242ac1200080000","toAccountId":"0000015cf9115782-0242ac1200080001","amount":50000000,"description":"test","status":"COMPLETED"}]
```






## Use Postman:


## Create new customer (C1):

POST URL: http://localhost:8083/v1/createcustomer

Header: Content-Type: application/json

Body:
```
{"name":{"firstName":"Google1222","lastName":"Com1"},"email":"aaa1.bbb1@google.com","password":"password","ssn":"9999999999","phoneNumber":"4166666666","address":{"street1":"Yonge St","street2":"2556 unit","city":"toronto","state":"ON","zipCode":"Canada","country":"L3R 5F5"}}
```

## Create an account with the customer (replace the customer id with real customer id):

POST URL: http://localhost:8081/v1/openaccount

Header: Content-Type: application/json

Body: {"customerId":"0000015cf4be6114-0242ac1200060001","title":"RRSP account","description":"account for testing","initialBalance":12355}


## Create an account (no link with customer)

POST URL: http://localhost:8081/v1/openaccount

Header: Content-Type: application/json

Body: {"title":"RRSP account","description":"account for testing","initialBalance":12355}


## Create new customer (C2):

POST URL: http://localhost:8083/v1/createcustomer

Header: Content-Type: application/json

Body:
```
{"name":{"firstName":"Google12","lastName":"Com1"},"email":"aaa2.bbb2@google.com","password":"password","ssn":"9999999999","phoneNumber":"4166666666","address":{"street1":"Yonge St","street2":"2556 unit","city":"toronto","state":"ON","zipCode":"Canada","country":"L3R 5F5"}}
```


## Link account to customer (replace the customer id and account with real Id):

POST URL: http://localhost:8083/v1/customers/toaccounts/0000015cf4b840f3-0242ac1200070000

Header: Content-Type: application/json

Body: {"id":"0000015cf4b080c7-0242ac1200070001","title":"title","owner":"google","description":"test case"}


## Transfer money from account (replace the from account and to account id with real id):

POST URL: http://localhost:8085/v1/transfers

Header: Content-Type: application/json

Body:{"fromAccountId":"2222-22222","toAccountId":"2222-22223","amount":5000,"description":"test"}


## Delete account:

DELETE URL: http://localhost:8081/v1/delete/0000015cf4bec29b-0242ac1200070001

Header: Content-Type: application/json




## Customer and Account view (use any browser)

--View the new customer by email (provide wildcard search):

  http://localhost:8084/v1/customers/aaa1.bbb1@google.com



--View the new customer by customer Id (replace the customer id with real customer id,. You can use copy from result of create customer)

  http://localhost:8084/v1/customer/{customerId}



-- view account by Id (replace the Id with real account Id)

http://localhost:8082/v1/accounts/{accountId}



-- View account transaction history:

http://localhost:8082/v1/accounts/0000015cf910e31b-0242ac1200080000/history



## End