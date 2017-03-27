### Hello Grpahql exmaple

To start the server:

```
git clone git@github.com:networknt/light-java-example.git
cd light-java-example/graphql/hello
mvn clean install exec:exec
```

To test the server: 

```
curl -H 'Content-Type:application/json' -XPOST http://localhost:8080/graphql -d '{"query":"{ hello }"}'
```

and the result is:

```
{"hello":"world"}
```

To access GraphiQL, you need to put the following url into your browser's address.

```
http://localhost:8080/graphql
```

On the left panel, enter the following to test the result.
 
```
{ hello }
```
