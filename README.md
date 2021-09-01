# Todo Challenge

This project provides the micro service implementation for Todo Domain Model.

## Development
This is a Java project and requires you have a Java JDK 1.11 or higher installed and
preferably an IDE for Java development. This project uses Gradle build system, you can
view all possible build actions for project via:
`./gradlew tasks -all` 

### Persistence
Each Domain entity is a separate Mongo DB collection.

### Tests

Tests for this project can be run from top folder of project in command line:
`./gradlew test`

### Build

There is a build script to create runtime artifacts from the source files for this project.
The build script is capable to generate several output formats of runtime distributables,
To build executable Java jar file output, execute the build script from top folder of project in command line:
`./gradlew assemble`

The compiled jar file will be located in a generated sub-directory under project root directory in `./build/lib`

### Run
Can use the Gradle build to run latest source code on the fly:
`./gradlew bootRun`

## Deployment
Run the compiled jar directly by invoking the JRE 'java' VM from command line:
`java -jar ./build/lib/challenge-0.0.1-SNAPSHOT.jar`

### Services

When the service is running via `./gradlew bootRun` or `java -jar ./build/lib/challenge-0.0.1-SNAPSHOT.jar`
The Todo Task Entity is accessible through the API provided as an HTTP service on
port 8081 - http://localhost:8081/api/tasks

API Docs and Interactive Console for Todo API are located at http://localhost:8081/swagger-ui.html

### API Health Check
API provides a HTTP Health check endpoint:
GET http://localhost:8081/actuator/health

### Configuration Properties
The project uses Spring Boot, which enables externalized properties to be passed
at JVM runtime through O/S, shell environment variables or as JVM system properties. 
Refer to 'src/main/resources/application.properties' for names of variables that can be set.

## Status 
1. Task Entity is implemented, model, persistence, CRUD api and docs.
2. Added multi-field search endpoint with simple query string DSL, 
   allows for query  that filters Tasks down on Status enum values.
3. Added full text search endpoint, refer to api docs.



