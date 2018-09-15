With release 1.5.19, we have three different ways to configure middleware handlers. These options give us flexibility to configure multiple middleware chains for each HTTP endpoint. This allows us easily bypass the OAuth 2.0 security and enable IP whitelist for /health/{serviceId} from Consul and /server/info from light-portal, serve static content and SPA for the service instance, and also combine multiple frameworks into the same instance such as OpenAPI and GraphQL in the same JVM instance. 

The following is the descriptions of each approach. 

### HandlerProvider and MiddlewareHandler in service.yml

This is the original approach to configure middleware handlers. We keeps it for backward compatibility for existing services. It is recommend to use the other two approaches when you start a new project. The light-codegen generates an implementation of HandlerProvider with all the endpoints and the handlers mapping. Also a list of middleware handlers which implements com.networknt.handler.MiddlewareHandler will be wired in sequence for the request/response chain. 

This approach is very easy to implement and easy to understand. When a request comes in, it goes through all the middleware handler and then to the HandlerProvider implementation to route to the correct business handler for the endpoint. 

The drawbacks of this approach is that the entire service can only share one middleware handler chain. There is no way that you can have different security handler for /health/{serviceId} and there is no way you can mix both OpenAPI and GraphQL together in the same instance. 

Here is an example of service.yml

```
# Singleton service factory configuration/IoC injection
singletons:
# HandlerProvider implementation
- com.networknt.handler.HandlerProvider:
  - com.networknt.aa.PathHandlerProvider
# MiddlewareHandler implementations, the calling sequence is as defined in the request/response chain.
- com.networknt.handler.MiddlewareHandler:
  # Exception Global exception handler that needs to be called first to wrap all middleware handlers and business handlers
  - com.networknt.exception.ExceptionHandler
  # Metrics handler to calculate response time accurately, this needs to be the second handler in the chain.
  - com.networknt.metrics.MetricsHandler
  # Traceability Put traceabilityId into response header from request header if it exists
  - com.networknt.traceability.TraceabilityHandler
  # Correlation Create correlationId if it doesn't exist in the request header and put it into the request header
  - com.networknt.correlation.CorrelationHandler
  # Parsing OpenAPI 3.0 specification based on request uri and method.
  - com.networknt.openapi.OpenApiHandler
  # Security JWT token verification and scope verification (depending on OpenApiHandler)
  - com.networknt.openapi.JwtVerifyHandler
  # Body Parse body based on content type in the header.
  - com.networknt.body.BodyHandler
  # SimpleAudit Log important info about the request into audit log
  - com.networknt.audit.AuditHandler
  # Sanitizer Encode cross site scripting
  - com.networknt.sanitizer.SanitizerHandler
  # Validator Validate request based on OpenAPI 3.0 specification (depending on OpenApiHandler and BodyHandler)
  - com.networknt.openapi.ValidatorHandler
```

### Individual Chain per Endpoint

In release 1.5.18, we have introduced multiple middleware handler chains. That means each endpoint can define its chain individually in handler.yml configuration file. 

This gives us a lot of flexibility in defining chains. We can define a default chain with common middleware handlers and apply it to all the endpoint from the OpenAPI specification. Also, we can define other chains for /health/{serviceId} and /server/info. This allow us to refactor the light-codegen to remove the injection of common endpoints to the runtime specification and use the openapi.yaml directly in the runtime configuration from the model-config repository. 

Although the handler.yml is generated from the specification file, for big APIs, it looks very long as each endpoint has an entry. The good think is that we have the endpoint to handler mapping in the same place and there is no need to look into both Java source code and configuration to figure out the mapping. 


Here is an example of handler.yml

```
# Handler middleware chain configuration
---
enabled: true

#------------------------------------------------------------------------------
# Support individual handler chains for each separate endpoint. It allows framework
# handlers like health check, server info to bypass majority of the middleware handlers
# and allows mixing multiple frameworks like OpenAPI and GraphQL in the same instance.
#
# handlers  --  list of handlers to be used across chains in this microservice
#               including the routing handlers for ALL endpoints
#           --  format: fully qualified handler class name@optional:given name
# chains    --  allows forming of [1..N] chains, which could be wholly or
#               used to form handler chains for each endpoint
#               ex.: default chain below, reused partially across multiple endpoints
# paths     --  list all the paths to be used for routing within the microservice
#           ----  path: the URI for the endpoint (ex.: path: '/v1/pets')
#           ----  method: the operation in use (ex.: 'post')
#           ----  exec: handlers to be executed -- this element forms the list and
#                       the order of execution for the handlers
#
# IMPORTANT NOTES:
# - to avoid executing a handler, it has to be removed/commented out in the chain
#   or change the enabled:boolean to false for a middleware handler configuration.
# - all handlers, routing handler included, are to be listed in the execution chain
# - for consistency, give a name to each handler; it is easier to refer to a name
#   vs a fully qualified class name and is more elegant
# - you can list in chains the fully qualified handler class names, and avoid using the
#   handlers element altogether
#------------------------------------------------------------------------------
handlers:
  # Light-framework cross-cutting concerns implemented in the microservice
  - com.networknt.exception.ExceptionHandler@exception
  - com.networknt.metrics.MetricsHandler@metrics
  - com.networknt.traceability.TraceabilityHandler@traceability
  - com.networknt.correlation.CorrelationHandler@correlation
  - com.networknt.openapi.OpenApiHandler@specification
  - com.networknt.openapi.JwtVerifyHandler@security
  - com.networknt.body.BodyHandler@body
  - com.networknt.audit.AuditHandler@audit
  - com.networknt.sanitizer.SanitizerHandler@sanitizer
  - com.networknt.openapi.ValidatorHandler@validator
  # Customer business domain specific cross-cutting concerns handlers
  # - com.example.validator.CustomizedValidator@custvalidator
  # Framework endpoint handlers
  - com.networknt.health.HealthGetHandler@health
  - com.networknt.info.ServerInfoGetHandler@info
  # - com.networknt.metrics.prometheus.PrometheusGetHandler@getprometheus
  # Business Handlers
  - com.networknt.petstore.handler.PetsGetHandler
  - com.networknt.petstore.handler.PetsPostHandler
  - com.networknt.petstore.handler.PetsPetIdGetHandler
  - com.networknt.petstore.handler.PetsPetIdDeleteHandler


chains:
  default:
    - exception
    - metrics
    - traceability
    - correlation
    - specification
    - security
    - body
    - audit
    - sanitizer
    - validator

paths:
  - path: '/v1/pets'
    method: 'get'
    exec:
      - default
      - com.networknt.petstore.handler.PetsGetHandler
  - path: '/v1/pets'
    method: 'post'
    exec:
      - default
      - com.networknt.petstore.handler.PetsPostHandler
  - path: '/v1/pets/{petId}'
    method: 'get'
    exec:
      - default
      - com.networknt.petstore.handler.PetsPetIdGetHandler
  - path: '/v1/pets/{petId}'
    method: 'delete'
    exec:
      - default
      - com.networknt.petstore.handler.PetsPetIdDeleteHandler

  - path: '/health/com.networknt.petstore-3.0.1'
    method: 'get'
    exec:
      - health

  - path: '/server/info'
    method: 'get'
    exec:
      - info


```


### Endpoint source from Specification


In release 1.5.19, a new approach is contributed by @logi which uses the openapi.yaml to generate the endpoint source. This hides the mappings for the specification so that users won't even care about it. It simplify the handler.yml file a lot. 

For this to work, we only need to change the handler.yml file and the binary jar file is the same. 

Here is an example of handler.yml

```
# Handler middleware chain configuration
---
enabled: true

#------------------------------------------------------------------------------
# Support individual handler chains for each separate endpoint. It allows framework
# handlers like health check, server info to bypass majority of the middleware handlers
# and allows mixing multiple frameworks like OpenAPI and GraphQL in the same instance.
#
# handlers  --  list of handlers to be used across chains in this microservice
#               including the routing handlers for ALL endpoints
#           --  format: fully qualified handler class name@optional:given name
# chains    --  allows forming of [1..N] chains, which could be wholly or
#               used to form handler chains for each endpoint
#               ex.: default chain below, reused partially across multiple endpoints
# paths     --  list all the paths to be used for routing within the microservice
#           ----  path: the URI for the endpoint (ex.: path: '/v1/pets')
#           ----  method: the operation in use (ex.: 'post')
#           ----  exec: handlers to be executed -- this element forms the list and
#                       the order of execution for the handlers
#
# IMPORTANT NOTES:
# - to avoid executing a handler, it has to be removed/commented out in the chain
#   or change the enabled:boolean to false for a middleware handler configuration.
# - all handlers, routing handler included, are to be listed in the execution chain
# - for consistency, give a name to each handler; it is easier to refer to a name
#   vs a fully qualified class name and is more elegant
# - you can list in chains the fully qualified handler class names, and avoid using the
#   handlers element altogether
#------------------------------------------------------------------------------
handlers:
  # Light-framework cross-cutting concerns implemented in the microservice
  - com.networknt.exception.ExceptionHandler@exception
  - com.networknt.metrics.MetricsHandler@metrics
  - com.networknt.traceability.TraceabilityHandler@traceability
  - com.networknt.correlation.CorrelationHandler@correlation
  - com.networknt.openapi.OpenApiHandler@specification
  - com.networknt.openapi.JwtVerifyHandler@security
  - com.networknt.body.BodyHandler@body
  - com.networknt.audit.AuditHandler@audit
  - com.networknt.sanitizer.SanitizerHandler@sanitizer
  - com.networknt.openapi.ValidatorHandler@validator
  # Customer business domain specific cross-cutting concerns handlers
  # - com.example.validator.CustomizedValidator@custvalidator
  # Framework endpoint handlers
  - com.networknt.health.HealthGetHandler@health
  - com.networknt.info.ServerInfoGetHandler@info
  # - com.networknt.metrics.prometheus.PrometheusGetHandler@getprometheus
  # OpenApiPathHandler
  - com.networknt.petstore.handler.OpenApiPathHandler@openapi-handler


chains:
  default:
    - exception
    - metrics
    - traceability
    - correlation
    - specification
    - security
    - body
    - audit
    - sanitizer
    - validator

paths:
  - source: com.networknt.openapi.OpenApiEndpointSource
    exec: 
      - default
      - openapi-handler

  - path: '/health/com.networknt.petstore-3.0.1'
    method: 'get'
    exec:
      - health

  - path: '/server/info'
    method: 'get'
    exec:
      - info


```

As you can see, instead of listing all the business handlers, we have generated an OpenApiPathHandler as the final handler. The source code is something like this.

```
package com.networknt.petstore.handler;

import com.networknt.audit.AuditHandler;
import com.networknt.utility.Constants;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.util.Map;

/**
 * This is generated by the light-codegen
 */
public class OpenApiPathHandler implements HttpHandler {
    PetsGetHandler petsGetHandler = new PetsGetHandler();
    PetsPetIdGetHandler petsPetIdGetHandler = new PetsPetIdGetHandler();
    PetsPetIdDeleteHandler petsPetIdDeleteHandler = new PetsPetIdDeleteHandler();
    PetsPostHandler petsPostHandler = new PetsPostHandler();

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Map<String, Object> auditInfo = exchange.getAttachment(AuditHandler.AUDIT_INFO);
        String endpoint = (String)auditInfo.get(Constants.ENDPOINT_STRING);
        switch(endpoint) {
            case "/pets@get":
                petsGetHandler.handleRequest(exchange);
                break;
            case "/pets/{petId}@get":
                petsPetIdGetHandler.handleRequest(exchange);
                break;
            case "/pets@post":
                petsPostHandler.handleRequest(exchange);
                break;
            case "/pets/{petId}@delete":
                petsPetIdDeleteHandler.handleRequest(exchange);
                break;
            default:
                throw new Exception(String.format("Unsupported endpoint %s", endpoint));
        }
    }
}

```

### Performance Test

wrk -t4 -c128 -d30s https://localhost:8443 -s pipeline.lua --latency -- /v1/pets/111 16


* service-config

```
wrk -t4 -c128 -d30s https://localhost:8443 -s pipeline.lua --latency -- /v1/pets/111 16
Running 30s test @ https://localhost:8443
  4 threads and 128 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    41.08ms   48.43ms 574.77ms   86.08%
    Req/Sec    18.69k     3.71k   32.80k    72.91%
  Latency Distribution
     50%   21.24ms
     75%   54.92ms
     90%  108.41ms
     99%  218.64ms
  2220464 requests in 30.09s, 343.05MB read
Requests/sec:  73799.17
Transfer/sec:     11.40MB

```

* endpoint-individual

```
 wrk -t4 -c128 -d30s https://localhost:8443 -s pipeline.lua --latency -- /v1/pets/111 16
Running 30s test @ https://localhost:8443
  4 threads and 128 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    32.64ms   38.44ms 459.26ms   86.68%
    Req/Sec    19.81k     3.11k   31.20k    77.26%
  Latency Distribution
     50%   17.63ms
     75%   40.80ms
     90%   85.17ms
     99%  177.91ms
  2357504 requests in 30.08s, 364.22MB read
Requests/sec:  78361.89
Transfer/sec:     12.11MB

```

* endpoint-source

```
wrk -t4 -c128 -d30s https://localhost:8443 -s pipeline.lua --latency -- /v1/pets/111 16
Running 30s test @ https://localhost:8443
  4 threads and 128 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    36.29ms   42.86ms 513.59ms   86.52%
    Req/Sec    19.10k     2.95k   28.28k    73.22%
  Latency Distribution
     50%   19.34ms
     75%   46.37ms
     90%   95.02ms
     99%  197.14ms
  2266896 requests in 30.07s, 350.22MB read
Requests/sec:  75396.96
Transfer/sec:     11.65MB

```

### Summary

Above we have described all three approaches for defining middleware handler chains. The default one in the generator is the endpoint-individual which is the fastest. However, the performance difference is almost negligible. In the future, we can add a switch in the light-codegen config.json to give user an option to generate endpoint-source handler.yml and a final handler class. 



