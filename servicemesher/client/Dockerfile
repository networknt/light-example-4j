
FROM openjdk:11.0.3-slim
#EXPOSE 8448
ADD /target/marketsample-service-1.00.jar server.jar
CMD ["/bin/sh","-c","java -Dlight-4j-config-dir=/config -Dlogback.configurationFile=/config/logback.xml -jar /server.jar"]
