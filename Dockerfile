# ==========================
# Stage 1: Build the JAR
# ==========================
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy Maven files and build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================
# Stage 2: Run the App
# ==========================
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run the JAR with debug info
ENTRYPOINT ["sh", "-c", "echo 'DATABASE_URL=' $DATABASE_URL && java -jar app.jar"]
