# PDF example display

## Build and Start


## Build and verify

 ### Build and start service:


 ```
 cd ~/networknt
 git clone git@github.com:networknt/light-example-4j.git
 cd ~/networknt/light-example-4j/client/pdf

 mvn clean install

java -jar   target/pdf-service-1.00.jar

```

 ###  verify

from POSTMAN:

 GET: https://localhost:8449/v1/pdf/report

 Download response as pdf file and verify