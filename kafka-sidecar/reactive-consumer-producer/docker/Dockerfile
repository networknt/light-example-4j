
FROM azul/zulu-openjdk-alpine:11 as packager

RUN { \
        java --version ; \
        echo "jlink version:" && \
        $JAVA_HOME/bin/jlink --version ; \
    }

ENV JAVA_MINIMAL=/opt/jre

# build modules distribution
RUN $JAVA_HOME/bin/jlink \
    --verbose \
    --add-modules \
        java.base,java.sql,java.naming,java.desktop,java.xml,jdk.crypto.cryptoki,jdk.crypto.ec,jdk.unsupported,java.management,java.security.jgss,java.net.http \
    --compress 2 \
    --strip-debug \
    --no-header-files \
    --no-man-pages \
    --output "$JAVA_MINIMAL"

# Second stage, add only our minimal "JRE" distr and our app
FROM alpine

ENV JAVA_MINIMAL=/opt/jre
ENV PATH="$PATH:$JAVA_MINIMAL/bin"

COPY --from=packager "$JAVA_MINIMAL" "$JAVA_MINIMAL"


COPY /target/server.jar server.jar


CMD ["/bin/sh","-c","exec java -Dlight-4j-config-dir=/config -Dlogback.configurationFile=/config/logback.xml -jar /server.jar"]
