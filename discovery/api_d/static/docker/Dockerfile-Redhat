
FROM registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift
#EXPOSE  7444
ADD /target/apid-1.0.0.jar server.jar
CMD ["/bin/sh","-c","java -Dlight-4j-config-dir=/config -Dlogback.configurationFile=/config/logback.xml -jar server.jar"]
