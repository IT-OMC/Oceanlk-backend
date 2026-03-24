FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
WORKDIR /workspace

# Cache dependencies first for faster rebuilds
COPY pom.xml .
RUN mvn -B -ntp dependency:go-offline

# Build application
COPY src ./src
RUN mvn -B -ntp clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Use non-root user in runtime image
RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=builder --chown=spring:spring /workspace/target/*.jar app.jar

ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod

USER spring:spring
EXPOSE 8080

# Cloud Run sends traffic to $PORT; start app on that port.
ENTRYPOINT ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError -jar app.jar --server.port=${PORT}"]
