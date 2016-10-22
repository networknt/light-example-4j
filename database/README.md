To build and start the example database.

## Environment

You have to make sure you have docker and docker compose installed.

## Build

```
mvn clean install
docker build -t networknt/example-database .
```
## Compose

```
docker-compose up
```