# servicemesh services module

## Start server

### Build and start server side services:

  ```
 cd ~/networknt
 git clone git@github.com:networknt/light-example-4j.git
 cd ~/networknt/light-example-4j/servicemesher/services

 mvn clean install -Prelease

 docker-compose -f docker-compose-consul.yml up

 
docker-compose  up

  ```