# Light-4j servicemesher client module call example

This example project used to show how to use light-4j client module and consumer component to call multi-service APIs in servicemesher environment.

It use java async concurrent feature and process the service call parallel



## Introduction:






![diagram](doc/servicemesher.png)


## Modules

 -- services

   Inside services folder, we build four simple server side mock service APIs:


```

| ------------------ | -------------------------------------------|------------| ----------------------|
| Service API        | Service Id                                 |  Port      |  docker image name    |
| ------------------ | -------------------------------------------|------------| ----------------------|
| petstore   API     | com.networknt.petstore-service-api-3.0.1   | 8443       | petstore-service      |
| ------------------ | -------------------------------------------|------------| ----------------------|
| bookstore API      | com.networknt.bookstore-service-api-3.0.1  | 8444       | bookstore-service     |
| ------------------ | -------------------------------------------|------------| ----------------------|
| foodstore API      | com.networknt.foodstore-service-api-3.0.1  | 8447       | foodstore-service     |
| ------------------ | -------------------------------------------|------------| ----------------------|
| computerstore API  |com.networknt.computerstore-service-api-3.0.1| 8445       | computerstore-service |
| ------------------ | -------------------------------------------|------------| ----------------------|


```

  -- client

    In client folder, we build a client service API which use light-4j client module's consumer component to call the service APIs parallel.



##  Structure diagram



## Build and verify

 ### Build and start server side services:

 Step 1 (build server side services and start local consul):

 ```
 cd ~/networknt
 git clone git@github.com:networknt/light-example-4j.git
 cd ~/networknt/light-example-4j/servicemesher/services

 mvn clean install

 docker-compose -f docker-compose-consul.yml up

 ```

  Step 2 (start server side services by open new terminal):

   ```
    cd ~/networknt/light-example-4j/servicemesher/services
   docker-compose  up

   ```

 ### Build and start client side service:

  ```
  cd ~/networknt
  git clone git@github.com:networknt/light-example-4j.git
  cd ~/networknt/light-example-4j/servicemesher/client

  mvn clean install

  docker-compose  up

  ```

  ### verify service:

  From browser or Postman (GET):

   ```
     https://localhost:8448/v1/market

   ```

   The client side service will call all four server side services parallel and get the result to build a market object. the same result will like below:

    ```
     {
         "name": null,
         "pets": [
             {
                 "id": 1,
                 "name": "catten",
                 "tag": "cat"
             },
             {
                 "id": 2,
                 "name": "doggy",
                 "tag": "dog"
             }
         ],
         "books": [
             {
                 "id": 1,
                 "name": "Educated",
                 "author": "James Bond"
             },
             {
                 "id": 2,
                 "name": "The Amazon Job",
                 "author": "Randy Grieser"
             }
         ],
         "foodbox": [
             {
                 "id": 1,
                 "name": "Noodle",
                 "tag": "food"
             },
             {
                 "id": 2,
                 "name": "Rice",
                 "tag": "food"
             }
         ],
         "computers": [
             {
                 "id": 1,
                 "brand": "HP",
                 "tag": "1112222-22"
             },
             {
                 "id": 2,
                 "brand": "IBM",
                 "tag": "111122255-55"
             }
         ]
     }

     ```