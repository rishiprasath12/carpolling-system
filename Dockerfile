# Multi-stage build for optimized image size
# Note: Using standard images (not alpine) for better platform compatibility
# Works on both AMD64 (Intel/AMD) and ARM64 (Apple Silicon M1/M2/M3/M4)

# Stage 1: Build the application
FROM maven:3.9.9-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies (for better caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre

# Set working directory
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/carpolling-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8010

# Set environment variables (can be overridden at runtime)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/carpolling_db
ENV SPRING_DATASOURCE_USERNAME=admin
ENV SPRING_DATASOURCE_PASSWORD=admin

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

