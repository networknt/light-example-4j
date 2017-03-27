# Relay Todo with a light-java-graphql server

This is a port of the [Relay TodoMVC example](https://github.com/graphql-java/todomvc-relay-java)
where the GraphQL server is replaced with [light-java-graphql](https://github.com/networknt/light-java-graphql).


### Running

Starting the frontend:

```bash
cd app
npm install
npm start
```

Starting the backend: (running on port 8080)

```bash
mvn clean install exec:exec
```

The app is now available at http://localhost:3000

Note: the Javascript app cannot auto refresh so you have to manually refresh the app to see the updates.


