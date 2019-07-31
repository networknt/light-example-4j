FROM openjdk:11.0.3-slim
#EXPOSE 8445
ADD /target/computerstore-service-api-3.0.1.jar server.jar
CMD ["/bin/sh","-c","java -Dlight-4j-config-dir=/config -Dlogback.configurationFile=/config/logback.xml -jar /server.jar"]