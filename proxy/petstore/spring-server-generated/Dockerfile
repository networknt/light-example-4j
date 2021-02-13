FROM openjdk:8-jre-alpine
ADD /target/swagger-spring-1.0.0.jar server.jar
CMD ["/bin/sh","-c","java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -jar /server.jar"]
