FROM insideo/jre8
EXPOSE 8080
ADD /target/webserver-0.1.0.jar server.jar
ADD /src/main/resources/public /public
CMD ["/bin/sh","-c","java -Dlight-java-config-dir=/config -jar /server.jar"]
