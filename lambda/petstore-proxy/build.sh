#!/bin/bash

set -ex

VERSION=$1
IMAGE_NAME="lambda-proxy"

showHelp() {
    echo " "
    echo "Error: $1"
    echo " "
    echo "    build.sh [VERSION]"
    echo " "
    echo "    where [VERSION] version of the docker image that you want to publish (example: 0.0.1)"
    echo " "
    echo "    example: ./build.sh 0.0.1"
    echo " "
}

build() {
    echo "Copying ..."
    # download from github release page
    wget https://github.com/networknt/light-proxy/releases/download/2.0.23/light-proxy.jar
    echo "Successfully copied!"
}

remove() {
    if [[ "$(docker images -q $IMAGE_NAME 2> /dev/null)" != "" ]]; then
        echo "Removing old $IMAGE_NAME images"
        docker images | grep $IMAGE_NAME | awk '{print $3}' | xargs docker rmi -f
        echo "Cleanup completed!"
    fi
}

publish() {
    echo "Building Docker image with version $VERSION"
    docker build -t $IMAGE_NAME -f ./Dockerfile-proxy .
    echo "Images built with version $VERSION"
    docker tag $IMAGE_NAME:latest 964637446810.dkr.ecr.us-east-2.amazonaws.com/lambda-proxy:latest
    echo "Pushing image to DockerHub"
    docker push 964637446810.dkr.ecr.us-east-2.amazonaws.com/lambda-proxy:latest
    echo "Image successfully published!"
}

cleanup() {
    echo "Clean up the proxy.jar"
    rm light-proxy.jar
}

if [ -z $VERSION ]; then
    showHelp "[VERSION] parameter is missing"
    exit
fi

build;
remove;
publish;
cleanup;
