
FROM openjdk:11.0.3-slim


COPY /target/server.jar server.jar


CMD ["/bin/sh","-c","exec java -Dlight-4j-config-dir=/config -Dlogback.configurationFile=/config/logback.xml -jar /server.jar"]
