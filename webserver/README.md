# Introduction
A web server implementation of light-java that provides two API endpoints and serving static content from
an extenalized folder. It can be single page javascript application alone with some images, fonts etc.

/text

returns "Hello World"

/json

returns {"message":"Hello World"}

# Getting Started

Build locally with JDK8 and Maven installed. 

```
git clone git@github.com:networknt/light-java-example.git

cd light-java-example/webserver

mvn clean install

cd target

java -jar webserver-x.x.x.jar

```

Start with Docker.

```
docker run -d -p 8080:8080 networknt/example-webserver
```

# Test

The two endpoints are protected by OAuth2 but it is disabled in the default 
configuration in security.json. In order to turn it on, you need to update
/src/main/resources/config/security.json to set enableVerifyJwt to true.

If you start the server in docker, you need to create a local folder and copy
security.json to that folder and update enableVerifyJwt to true. Let's assume
the folder that contains security.json is /tmp/config

```
docker run -d -v /tmp/config:/config -p 8080:8080 networknt/example-webserver
```

In order to access the demo server you can use a long lived token below issued by my
oauth2 server [light-oauth2](https://github.com/networknt/light-oauth2)

```
Bearer eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJ1cm46Y29tOm5ldHdvcmtudDpvYXV0aDI6djEiLCJhdWQiOiJ1cm46Y29tLm5ldHdvcmtudCIsImV4cCI6MTc4ODEzMjczNSwianRpIjoiNWtyM2ZWOHJaelBZNEJrSnNYZzFpQSIsImlhdCI6MTQ3Mjc3MjczNSwibmJmIjoxNDcyNzcyNjE1LCJ2ZXJzaW9uIjoiMS4wIiwidXNlcl9pZCI6InN0ZXZlIiwidXNlcl90eXBlIjoiRU1QTE9ZRUUiLCJjbGllbnRfaWQiOiJkZGNhZjBiYS0xMTMxLTIyMzItMzMxMy1kNmYyNzUzZjI1ZGMiLCJzY29wZSI6WyJhcGkuciIsImFwaS53Il19.gteJiy1uao8HLeWRljpZxHWUgQfofwmnFP-zv3EPUyXjyCOy3xclnfeTnTE39j8PgBwdFASPcDLLk1YfZJbsU6pLlmYXLtdpHDBsVmIRuch6LFPCVQ3JdqSQVci59OhSK0bBThGWqCD3UzDI_OnX4IVCAahcT9Bu94m5u_H_JNmwDf1XaP3Lt4I34buYMuRD9stchsnZi-tuIRkL13FARm1XA9aPZUMUXFdedBWDXo1zMREQ_qCJXOpaZDJM9Im0rIkq9wTEVU00pbRp_Vcdya3dfkFteBMHiwFVt6VNQaco5BXURDAIzXidwQxNEbX1ek03wra8AIani65ZK7fy_w
```

Postman is the best tool to test REST APIs

Add the following header in the request.

Authorization with value as above token.

GET localhost:8080/text

GET localhost:8080/json

