
FROM openjdk:8-jre-alpine
#EXPOSE  8443
ADD /target/merger-1.0.0.jar server.jar
CMD ["/bin/sh","-c","java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -Dlight-4j-config-dir=/config -Dlogback.configurationFile=/config/logback.xml -cp /server.jar:/service/* com.networknt.server.Server"]
