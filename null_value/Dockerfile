FROM insideo/jre8
EXPOSE 8080
ADD /target/demo-value-1.0.0.jar server.jar
CMD ["/bin/sh","-c","java -Dlight-java-config-dir=/config -jar /server.jar"]
