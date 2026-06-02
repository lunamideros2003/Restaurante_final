# syntax=docker/dockerfile:1.7

# ============================================================================
# Stage 1: Build the Spring Boot fat JAR with Maven
# ============================================================================
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build

# Cache Maven dependencies in a separate layer
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -B -q dependency:go-offline

# Build the application
COPY src src
RUN ./mvnw -B -q -DskipTests clean package \
 && cp target/pedidos-0.0.1-SNAPSHOT.jar /app.jar

# ============================================================================
# Stage 2: Minimal JRE runtime
# ============================================================================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Render inyecta PORT en runtime (default 10000 para Docker)
ENV PORT=8080 \
    SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS=""

EXPOSE 8080

COPY --from=build /app.jar /app/app.jar

# Usuario no-root por seguridad
RUN addgroup -S spring && adduser -S spring -G spring \
 && chown -R spring:spring /app
USER spring:spring

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
