
FROM registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift
#EXPOSE 8448
ADD /target/marketsample-service-3.0.1.jar server.jar
CMD ["/bin/sh","-c","java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -Dlight-4j-config-dir=/config -Dlogback.configurationFile=/config/logback.xml -jar server.jar"]
