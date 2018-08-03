
FROM registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift
#EXPOSE  7441
ADD /target/apia-1.0.0.jar server.jar
CMD ["/bin/sh","-c","java -Dlight-4j-config-dir=/config -Dlogback.configurationFile=/config/logback.xml -jar server.jar"]
