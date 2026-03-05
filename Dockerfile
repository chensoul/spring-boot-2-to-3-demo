# Multi-stage build for spring-boot-2-to-3-demo (Spring Boot 2.7 / Java 11).
# After upgrading to Boot 3, change base images to eclipse-temurin:21-jdk and eclipse-temurin:21-jre.

FROM maven:3.9-eclipse-temurin-11-alpine AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B || true

COPY src src
RUN mvn package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:11-jre-alpine
WORKDIR /app

RUN adduser -D -s /bin/sh app
USER app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
