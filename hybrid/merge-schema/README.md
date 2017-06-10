light-codegen generated projects for schema merge test

- server

A generic server that will host service1.jar and service2.jar to ensure
that schema.json in both jar file can be merged together in order to
validate request in hybrid-router json handler. 

- service1 

A generic service project with only one service defined. 

- service2

A generic service project with only one service defined. 


In light-hybrid-4j, these service1 and service2 will be built into two
simple jars and deployed in the classpath of server which can be started
with command line "mvn clean install exec:exec".

For service schema validation to work, both schema.json in service1.jar
and service2.jar will be loaded and merged together to perform validation
for incoming requests. 

The test projects light-codegen configs and schemas can be found in model-config/hybrid/merge-schema


