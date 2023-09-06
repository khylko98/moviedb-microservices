FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} config_server.jar
ENTRYPOINT ["java","-jar","config_server.jar"]
