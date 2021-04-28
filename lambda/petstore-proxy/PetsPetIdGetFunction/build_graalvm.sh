
#!/bin/bash

docker run --rm --name graal -v $(pwd):/working springci/graalvm-ce:master-java11 \
    /bin/bash -c "native-image \
                    --enable-url-protocols=http,https \
                    --no-fallback \
                    --allow-incomplete-classpath \
                    --enable-all-security-services \
                    -H:ReflectionConfigurationFiles=/working/reflect.json \
                    -H:ResourceConfigurationFiles=/working/resource-config.json \
                    -H:+ReportExceptionStackTraces \
                    -jar /working/build/libs/PetsPetIdGetFunction-all.jar \
                    ; \
                    cp PetsPetIdGetFunction-all /working/build/graalvm/server"

mkdir -p build/graalvm
if [ ! -f "build/graalvm/server" ]; then
    echo "there was an error building graalvm image"
    exit 1
fi
