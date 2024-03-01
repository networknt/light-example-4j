There are two examples to show how to use websocket with light-4j framework.


## client-to-server
This example to show you how to communicate between client and server. To start
the server.

```
cd ~/networknt
git clone git@github.com:networknt/light-example-4j.git
cd light-example-4j/websocket/client-to-server
mvn clean install exec:exec
```

Go to your browser and input the url

```
http://localhost:8080
```

## peer-to-peer

This is a chat server that shows how to use websocket to communicate between
two or more browsers.


```
cd ~/networknt
git clone git@github.com:networknt/light-example-4j.git
cd light-example-4j/websocket/peer-to-peer
mvn clean install exec:exec
```

Open two browsers on the same computer or two different computers with the url

```
http://localhost:8080
```
