## Kafka sidecar sample light-4j API

This is sample backend API for Kafka sidecar. It build with light-4j and it only use for reference purpose.

Fo the local testing, please refer to  kafka sidecar readme:

https://github.com/networknt/kafka-sidecar

### Endpoint includes:

- path: '/kafka/records'
  
  method: 'POST'
  
Kafka sidecar Reactive consumer backend API. In the kafka sidecar kafka-consumer.yml config file, user can specify the backend API host url and API path:

```text
kafka-consumer.backendApiHost: http://localhost:8080
kafka-consumer.backendApiPath: /kafka/records
```
When the Reactive consumer consume the records from kafka, it will call backend API to process the event messaage.

-----------
- path: '/kafka/ksqldb'
  
  method: 'GET'

API endppoint to call kafka sidecar KsqlDB active consumer endpoint to run ksql pull/push query.

Queries are defined in the values.yml:

```text
api-config.apiQuery:
  userQuery: select * from QUERYUSER1\

```

Pass the 'queryname' as required query parameter, and 'id' is option parameter.

if id parameter is not provided, the ksql query will run as full table scan.

To test this endpoint, please refer kafka-sidecar to create ksql TKable (QUERYUSER1) (CREATE TABLE AS SELECT...) on kafka:

https://github.com/networknt/kafka-sidecar/blob/master/doc/ksql.md


Sample request curl command:

```text
curl --location --request GET 'https://localhost:8444/kafka/ksqldb?queryname=userQuery&id=1' \
--data-raw '';
```