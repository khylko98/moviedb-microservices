FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} movie-service.jar
ENTRYPOINT ["java","-jar","movie-service.jar"]
