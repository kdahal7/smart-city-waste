# Dockerfile for Spring Boot Application

# Build stage
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Production stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/waste-optimization-1.0.0.jar app.jar

# Expose port (Render will set PORT env var)
EXPOSE 8080

# Run the application
CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
