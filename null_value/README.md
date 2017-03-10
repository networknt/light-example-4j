# Nullable Value Demo

This is a demo to show users how to construct OpenAPI specification to allow
certain field to be null. If only one data type is defined and a null value
is passed in, the built-in [JSON schema validator](https://github.com/networknt/json-schema-validator)
will report an error.

In this demo we have a post endpoint with start date in the body. In order
to allow startDate to be null, we have to define this field in yaml as

```
     startDate:
        type:
        - "string"
        - "null"
        format: "date"

```

And corresponding JSON field is

```
                "startDate": {
                    "type": [
                        "string",
                        "null"
                    ],
                    "format": "date"
                },

```


## Start server

Run with

```
mvn package exec:exec
``

## Test

As this is just a simple demo, no security definitions in the spec. The post
endpoint url is

```
localhost:8080/v1/data
```

Header Content-Type must be set as application/json

Content-Type=application/json


And you can use this JSON object as request body.

```
{
	"id": 123,
	"name": "king",
	"weight": 76.22,
	"startDate": null
}
```
