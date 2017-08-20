#!/bin/bash

# The following commands remove all eventuate containers and images in order to start a brand new build.
# It should be called if you have changed any code in one of services.

docker ps -a | awk '{ print }' | grep accountmanagement | awk '{print $1}' | xargs -I {} docker rm {}
docker images | awk '{ print }' | grep accountmanagement | awk '{print $3}' | xargs -I {} docker rmi {}
