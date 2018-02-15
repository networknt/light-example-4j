# GraphQL Mutation Example

## Build and Start

```
// windows
gradlew.bat run
// *nix
./gradlew run 
```

## Test the server with GraphiQL

Open your browser and point to 

```
localhost:8080/graphql
```
Now you can explore the schema on Documentation Explorer. There should be a query and a mutation.

## Test query with GraphiQL

```
query {
  numberHolder {
    theNumber
  }
}
```

## Test mutation with GraphiQL

```
mutation {
  changeTheNumber(newNumber:4) {
    theNumber
  }
}
```

## Test mutation with variables

Query: 

```
mutation ($n: Int!) {
  changeTheNumber(newNumber: $n) {
    theNumber
  }
}

```
Variables
```
{"n": 5}
```
