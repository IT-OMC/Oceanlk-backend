# ============ BUILD STAGE ============
# Multi-stage build to reduce final image size
FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copy pom.xml and download dependencies (layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -q

# ============ RUNTIME STAGE ============
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Copy the built JAR from builder stage
COPY --from=builder --chown=spring:spring /app/target/*.jar app.jar

# Switch to non-root user
USER spring:spring

# Set default port for GCP Cloud Run (can be overridden via PORT env var)
ENV PORT=8080 \
    SERVER_PORT=8080

# Expose port (for local development; Cloud Run will use PORT env var)
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget -q --spider http://localhost:${PORT}/actuator/health || exit 1

# Optimized JVM configuration for GCP Cloud Run:
# -Djava.security.egd              : Faster secure random generation
# -XX:+UseContainerSupport         : Respect cgroup limits
# -XX:MaxRAMPercentage=75.0        : Use 75% of available memory for heap
# -XX:+ExitOnOutOfMemoryError      : Fail fast on OOM
# -Dspring.profiles.active=cloud-run : Use Cloud Run-optimized configuration
# Application will listen on $PORT from Cloud Run environment (defaults to 8080)
ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+ExitOnOutOfMemoryError", \
  "-Dspring.profiles.active=cloud-run", \
  "-jar", "app.jar"]
