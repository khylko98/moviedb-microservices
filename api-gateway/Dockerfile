FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} api-gateway.jar
ENTRYPOINT ["java","-jar","api-gateway.jar"]
