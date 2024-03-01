
FROM openjdk:8-jre-alpine
EXPOSE  7442
ADD /target/apib-1.0.0.jar server.jar
CMD ["/bin/sh","-c","java -Dlight-4j-config-dir=/config -Dlogback.configurationFile=/config/logback.xml -jar /server.jar"]
