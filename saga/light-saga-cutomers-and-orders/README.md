# Light-saga-4j example


## Customer-Order example

The Customer-Order saga prove of concept example  is built using:

Java

light-4j

light-Tram-4j

Kafka

MySQL




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


```
cd ~/networknt
git clone git@github.com:networknt/light-example-4j.git
cd ~/networknt/light-example-4j/saga/light-saga-cutomers-and-orders
mvn clean install
docker-compose up
```


Verify result:

1. Create Customer :

```
curl -X POST \
  http://localhost:8081/v1/customers \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -d '{"name":"test-customer","creditLimitMoney":{"amount":200},"creditLimit":"200"}'
```

2. Create Order (replace the customer Id from the response by create customer request above )

```
curl -X POST \
  http://localhost:8082/v1/orders \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -d '{"orderTotal":"20","customerId":11111,"orderTotalMoney":{"amount":20}}
```
