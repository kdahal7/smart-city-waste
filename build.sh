#!/bin/bash
# Render.com Build Script for Smart City Waste Optimization Dashboard

echo "🚀 Starting build process..."

# Install Maven if not present
if ! command -v mvn &> /dev/null; then
    echo "📦 Installing Maven..."
    chmod +x ./mvnw
fi

# Clean and build the project
echo "🔨 Building Spring Boot application..."
./mvnw clean package -DskipTests

echo "✅ Build completed successfully!"
echo "📦 JAR file: target/waste-optimization-1.0.0.jar"
