FROM openjdk:8-jre-alpine
ADD /target/api-simple-web-0.1.0.jar server.jar
ADD /src/main/resources/public /public
CMD ["/bin/sh","-c","java -Dlight-4j-config-dir=/config -Dlogback.configurationFile=/config/logback.xml -jar /server.jar"]
