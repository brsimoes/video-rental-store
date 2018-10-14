# Video Rental Store

## Architecture

The application architecture is based on three well established architectural design patterns: Ports & Adapters (aka Hexagonal Architecture), CQRS and Event Sourcing.  I've splitted the application core into three bounded contexts: Users, Movie catalog and Rentals, each one packed as module.

The application core of each module is composed by an application layer, a domain layer and persistence layer. There's also a fourth module that implements an adapter for the application that consists on a RESTful API.

## Code Structure

The project contains a parent project `video-rental-store`that groups 4 modules:
* video-rental-store-catalog - The module responsible to manage the collection of videos available for renting.

* video-rental-store-user - The module responsible to manage users of the video-rental-store.

* video-rental-store-rentals - The module responsible to create rentals and then add movies and return them.

* video-rental-store-restapi - An implementation of RESTful API that groups the features provided by Users, Movies and Rentals modules.

The modules that represent each bounded context follow this packaging structure:
* com.casumo.videorentalstore.XXX.core.application - Orchestration of the use cases than be triggered on the application.
* com.casumo.videorentalstore.XXX.core.domain - Logic and data to support the business rules. 
* com.casumo.videorentalstore.XXX.core.persistence - Represents the interfaces (Repositories) required for storing data by the application core.
* com.casumo.videorentalstore.XXX.core.port - Represents the interface for the features provided by the application core.

## Implementation details

I've made use of the following frameworks in order to ease the developments:

* [Spring Boot](https://spring.io/projects) - For REST and HATEOAS support, beyond others features provided by spring framework.
* [Axon](https://axoniq.io) - For event sourcing and CQRS support.
* [Springfox](http://springfox.github.io/springfox/) - For API documentation


Throughout the application life, data is stored on an in-memory database. There two models persisted, one for writes, implemented as aggregates, following the concepts of DDD and stored as a set of events, and another model, for reads, following a conventional relational model.

The interaction with the domain model of each bonded context is done by publishing commands on a internal bus.

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Build

```shell
mvn clean install
```

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.casumo.videorentalstore.App` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
cd restapi && mvn spring-boot:run
```

You can then access to the URL http://localhost:8080/api/swagger-ui.html#/

## ToDo / Next steps

- Implement authorization schema on the API
- Implement delete operations for all resources.
- Split the monolithic application into a set of microservices (one for each bounded context)
- Improve code coverage of unit/integration tests
- Improve API documentation
