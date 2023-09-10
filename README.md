# Spring Boot Microservices Project (MovieDB)

This is a sample Spring Boot microservices project with the following services:

- Service Registry (Eureka Server)
- Config Server (Cloud Config Server)
- API Gateway
- User Service
- Movie Service

To run all services, follow these steps:

## Prerequisites

1. Ensure you have Maven installed on your machine.
2. Install Docker on your PC.

## Building the Services

Inside each service's directory, run the following command to build the service:

```shell
mvn package
```

## Packeging the Services

1. Start the Service Registry (Eureka Server):

```shell
cd service-registry
mvn package
cd ..
```

2. Start the Config Server (Cloud Config Server):

```shell
cd config-server
mvn package
cd ..
```

3. Start the API Gateway:

```shell
cd api-gateway
mvn package
cd ..
```

4. Start the User Service:

```shell
cd user-service
mvn package
cd ..
```

5. Start the Movie Service:

```shell
cd movie-service
mvn package
cd ..
```

## Running with Docker Compose

Alternatively, you can use Docker Compose to run all services together:

1. Make sure you are in the project's root directory.
2. Run the following command:

```shell
docker compose up
```

This will start all services in Docker containers.

## Accessing the Services

- Service Registry (Eureka Server): http://localhost:8761
- Config Server (Cloud Config Server): http://localhost:8088
- API Gateway: http://localhost:8060
- User Service: http://localhost:8081
- Movie Service: http://localhost:8082
