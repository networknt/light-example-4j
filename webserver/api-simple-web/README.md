### Introduction

A web server implementation of light-4j that provides two API endpoints and 
serving static content from an externalized folder /public. It can be a single 
page javascript application alone with some images, fonts etc.

- /api/text

returns "Hello World"

- /api/json

returns {"message":"Hello World"}

- /

returns index.html

### Getting Started

##### Build locally with JDK8 and Maven installed. 

As the public folder must be an absolute path when running locally, before
building the server, you need to create a folder for your static files outside
of the project folder and update src/main/resources/config/webserver.yml to
specify base variable to that absolute folder. If you want to use the 
src/main/resources/public folder as your static file folder, you can set the
base as "/home/steve/networknt/light-example-4j/webserver/api-simple-web/src/main/resources/public"
which is the absolute folder on my desktop. It is commented out in the file. 

You can create a static index.html in this folder or deploy an SPA into
this folder.


With the configuration change, let's build and start the server.

```
git clone git@github.com:networknt/light-example-4j.git

cd light-example-4j/webserver/api-simple-web

mvn clean install

java -jar target/webserver-0.1.0.jar

```

Using this standalone server is only for testing or debuging and eventually we have 
to create a docker container and externalized the website so that you can update
the web pages or javascript application without changing the container. 


##### Create Docker Image

There are two Dockerfiles in docker folder and one is based on Alpine Linux and one is
based on RedHat Linux. A build.sh is available to build both docker images and publish
them to docker hub. 

```
./build.sh 0.1.0
```

The script will create docker images and publish to networknt docker account. You don't
need to run the script yourself and you can just pull the image from docker hub. 


##### Start with Docker.

Before start the server with docker run, please create a folder for your
static website. The following command use ~/public as static site folder.

You can create a static index.html in this folder or deploy an SPA into
this folder.

```
docker run -d -p 8443:8443 -v ~/website:/website networknt/api-simple-web-0.1.0
```

##### Externalize Config

The two endpoints are not protected by OAuth2 in the configuration. So the endpoints
can be access freely. If you want to change the config file in a externalized folder,
you can create these file in a folder like /tmp/config and then map this folder into
the container /config folder in order to override default config. 

```
docker run -d -v /tmp/config:/config -v /tmp/website:/website -p 8443:8443 networknt/api-simple-web-0.1.0
```

### Test

To test the service. 

JSON endpoint:

```
https://localhost:8443/api/json
```

TEXT endpoint:

```
https://localhost:8443/api/text
```

WEB:

```
https://localhost:8443/
```

