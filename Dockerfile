# ===== Stage 1: Build =====
FROM maven:3.9.4-eclipse-temurin-21 AS build

# Set working directory inside container
WORKDIR /app

# Copy pom.xml and download dependencies first (caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Build the app without running tests
RUN mvn clean package -DskipTests

# ===== Stage 2: Run =====
FROM eclipse-temurin:21-jre

# Set working directory inside container
WORKDIR /app

# Copy the JAR built in Stage 1
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
