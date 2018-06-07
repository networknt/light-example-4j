# Swagger Light Java Server

## Start server

Run with

```
mvn package exec:exec
```

Server started on `http://0.0.0.0:8080`

## Schema

```genericsql
schema {
  query: Query
  mutation: Mutation
}

type Query {
  numberHolder: NumberHolder
}

type NumberHolder {
  theNumber: Int
}

type Mutation {
  changeTheNumber(newNumber: Int!): NumberHolder
  failToChangeTheNumber(newNumber: Int): NumberHolder
}
```