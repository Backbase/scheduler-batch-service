# scheduler-batch-service

This service is used to  
   - 1. Store Scheduled Batch order record
   - 2. Get the Scheduled Batch Order record Based on next executionDate
   - 3. Update the Scheduled Batch Order based on Batch Order Id.
   - 4. Store the Batch Batch Order History record
   - 5. Get the batch Order record based on In put parameters
       - 1. executionDateRange
       - 2. batchOrderId
       - 3. accountNumber
       - 4. userName
       - 5. Status

#Getting Started
* [Extend and build](https://community.backbase.com/documentation/ServiceSDK/latest/extend_and_build)

## Dependencies

Requires a running Eureka registry, by default on port 8080.
Create the "batch-scheduler" schema in the database manually. Tables will creates along with change log when the service starts

## Configuration

Service configuration is under `src/main/resources/application.yaml`.

## Running

To run the service in development mode, use:
- `mvn spring-boot:run`

To run the service from the built binaries, use:
- `java -jar target/scheduler-batch-service-1.0.0-SNAPSHOT.jar`

## Authorization

Requests to this service are authorized with a Backbase Internal JWT, therefore you must access this service via the 
Backbase Gateway after authenticating with the authentication service.

For local development, an internal JWT can be created from http://jwt.io, entering `JWTSecretKeyDontUseInProduction!` 
as the secret in the signature to generate a valid signed JWT.

## Community Documentation

Add links to documentation including setup, config, etc.

## Jira Project

Add link to Jira project.

## Confluence Links
Links to relevant confluence pages (design etc).

## Support

The official scheduled-payment-order-service support room is [#s-scheduled-payment-order-service](https://todo).


 
