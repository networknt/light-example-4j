FROM openjdk:8-jre-alpine
EXPOSE  8080
ADD /build/libs/example-graphql-subscription-1.0.0-all.jar server.jar
CMD ["/bin/sh","-c","java -Dlight-4j-config-dir=/config -Dlogback.configurationFile=/config/logback.xml -jar /server.jar"]