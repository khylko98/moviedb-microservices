FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} service-registry.jar
ENTRYPOINT ["java","-jar","service-registry.jar"]
