

# market

This project contains source code and supporting files for a serverless application that you can deploy with the SAM CLI. It includes the following files and folders.


- StoreProductsGetFunction/src/main - Code for the application's StoreProductsGetFunction Lambda function.
- StoreProductsGetFunction/src/test - Unit tests for the application's StoreProductsGetFunction Lambda function.

- StoreProductsPostFunction/src/main - Code for the application's StoreProductsPostFunction Lambda function.
- StoreProductsPostFunction/src/test - Unit tests for the application's StoreProductsPostFunction Lambda function.

- events - Invocation events that you can use to invoke each function.
- template.yaml - A template that defines the application's AWS resources.
- public-vpc.yaml or private-vpc.yaml - VPC cluster template depending on launchType EC2 or Fargate and private or public subnet configuration.
- public-proxy.yaml or private-proxy.yaml - Lambda Proxy service template depending on the public subnet or private subnet.

## Deploy the public VPC

```
aws cloudformation create-stack --stack-name {stack-name} --template-body file://public-vpc.yaml --capabilities CAPABILITY_IAM
```

Example

```
aws cloudformation create-stack --stack-name petstore-vpc --template-body file://public-vpc.yaml --capabilities CAPABILITY_IAM
```

## Deploy the private VPC

```
aws cloudformation create-stack --stack-name {stack-name} --template-body file://private-vpc.yaml --capabilities CAPABILITY_IAM
```

Example

```
aws cloudformation create-stack --stack-name petstore-vpc --template-body file://private-vpc.yaml --capabilities CAPABILITY_IAM
```

## Describe stacks

```
aws cloudformation describe-stacks
```

## Delete stack

```
aws cloudformation delete-stack --stack-name {stack-name}
```


## Deploy the Proxy

```
aws cloudformation create-stack \
  --stack-name petstore-proxy \
  --template-body file://public-proxy.yaml \
  --parameters \
      ParameterKey=StackName,ParameterValue=petstore-vpc \
      ParameterKey=ServiceName,ParameterValue=lambda-proxy \
      ParameterKey=ImageUrl,ParameterValue=964637446810.dkr.ecr.us-east-2.amazonaws.com/lambda-proxy:latest \
      ParameterKey=ContainerPort,ParameterValue=8080 \
      ParameterKey=HealthCheckPath,ParameterValue=/health/com.networknt.petstore-3.0.1 \
      ParameterKey=HealthCheckIntervalSeconds,ParameterValue=90 \
      ParameterKey=AccessKeyId,ParameterValue=AKIA6BGG7KKNEZY2XFNS \
      ParameterKey=SecretAccessKey,ParameterValue=
```



The application uses several AWS resources, including Lambda functions and an API Gateway API. These resources are defined in the `template.yaml` file in this project. You can update the template to add AWS resources through the same deployment process that updates your application code.

If you prefer to use an integrated development environment (IDE) to build and test your application, you can use the AWS Toolkit.
The AWS Toolkit is an open source plug-in for popular IDEs that uses the SAM CLI to build and deploy serverless applications on AWS. The AWS Toolkit also adds a simplified step-through debugging experience for Lambda function code. See the following links to get started.

* [PyCharm](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [IntelliJ](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [VS Code](https://docs.aws.amazon.com/toolkit-for-vscode/latest/userguide/welcome.html)
* [Visual Studio](https://docs.aws.amazon.com/toolkit-for-visual-studio/latest/user-guide/welcome.html)

## Deploy the sample application

The Serverless Application Model Command Line Interface (SAM CLI) is an extension of the AWS CLI that adds functionality for building and testing Lambda applications. It uses Docker to run your functions in an Amazon Linux environment that matches Lambda. It can also emulate your application's build environment and API.

To use the SAM CLI, you need the following tools.

* SAM CLI - [Install the SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html)
* Java11 - [Install the Java 11](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html)
* Maven - [Install Maven](https://maven.apache.org/install.html)
* Docker - [Install Docker community edition](https://hub.docker.com/search/?type=edition&offering=community)

To build and deploy your application for the first time, run the following in your shell:

```bash
sam build
sam deploy --guided
```

The first command will build the source of your application. The second command will package and deploy your application to AWS, with a series of prompts:

* **Stack Name**: The name of the stack to deploy to CloudFormation. This should be unique to your account and region, and a good starting point would be something matching your project name.
* **AWS Region**: The AWS region you want to deploy your app to.
* **Confirm changes before deploy**: If set to yes, any change sets will be shown to you before execution for manual review. If set to no, the AWS SAM CLI will automatically deploy application changes.
* **Allow SAM CLI IAM role creation**: Many AWS SAM templates, including this example, create AWS IAM roles required for the AWS Lambda function(s) included to access AWS services. By default, these are scoped down to minimum required permissions. To deploy an AWS CloudFormation stack which creates or modified IAM roles, the `CAPABILITY_IAM` value for `capabilities` must be provided. If permission isn't provided through this prompt, to deploy this example you must explicitly pass `--capabilities CAPABILITY_IAM` to the `sam deploy` command.
* **Save arguments to samconfig.toml**: If set to yes, your choices will be saved to a configuration file inside the project, so that in the future you can just re-run `sam deploy` without parameters to deploy changes to your application.

You can find your API Gateway Endpoint URL in the output values displayed after deployment.

## Use the SAM CLI to build and test locally

Build your application with the `sam build` command.

```bash
$ sam build
```

The SAM CLI installs dependencies defined in `pom.xml` in each function folder, creates a deployment package, and saves it in the `.aws-sam/build` folder.

Test a single function by invoking it directly with a test event. An event is a JSON document that represents the input that the function receives from the event source. Test events are included in the `events` folder in this project.

Run functions locally and invoke them with the `sam local invoke` command.

```bash

$ sam local invoke StoreProductsGetFunction --event events/eventStoreProductsGetFunction.json

$ sam local invoke StoreProductsPostFunction --event events/eventStoreProductsPostFunction.json

```

The SAM CLI can also emulate your application's API. Use the `sam local start-api` to run the API locally on port 3000.

```bash
$ sam local start-api
$ curl http://localhost:3000/
```

The SAM CLI reads the application template to determine the API's routes and the functions that they invoke. The `Events` property on each function's definition includes the route and method for each path.

```yaml
      Events:

        StoreProductsGetFunction:
          Type: Api
          Properties:
            Path: //{store}/products
            Method: GET

        StoreProductsPostFunction:
          Type: Api
          Properties:
            Path: //{store}/products
            Method: POST

```

## Add a resource to your application
The application template uses AWS Serverless Application Model (AWS SAM) to define application resources. AWS SAM is an extension of AWS CloudFormation with a simpler syntax for configuring common serverless application resources such as functions, triggers, and APIs. For resources not included in [the SAM specification](https://github.com/awslabs/serverless-application-model/blob/master/versions/2016-10-31.md), you can use standard [AWS CloudFormation](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-template-resource-type-ref.html) resource types.

## Fetch, tail, and filter Lambda function logs

To simplify troubleshooting, SAM CLI has a command called `sam logs`. `sam logs` lets you fetch logs generated by your deployed Lambda function from the command line. In addition to printing the logs on the terminal, this command has several nifty features to help you quickly find the bug.

`NOTE`: This command works for all AWS Lambda functions; not just the ones you deploy using SAM.

```bash

$ sam logs -n StoreProductsGetFunction --stack-name {stack-name} --tail

$ sam logs -n StoreProductsPostFunction --stack-name {stack-name} --tail

```

You can find more information and examples about filtering Lambda function logs in the [SAM CLI Documentation](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-logging.html).

## Unit tests

Tests are defined in the `/src/test` folder for each function in this project.

```bash

$ cd StoreProductsGetFunction
$ mvn test

$ cd StoreProductsPostFunction
$ mvn test

```

## Cleanup

To delete the sample application that you created, use the AWS CLI. Assuming you used your project name for the stack name, you can run the following:

```bash
aws cloudformation delete-stack --stack-name {stack-name}
```

## Resources

See the [AWS SAM developer guide](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/what-is-sam.html) for an introduction to SAM specification, the SAM CLI, and serverless application concepts.

Next, you can use AWS Serverless Application Repository to deploy ready to use Apps that go beyond hello world samples and learn how authors developed their applications: [AWS Serverless Application Repository main page](https://aws.amazon.com/serverless/serverlessrepo/)
