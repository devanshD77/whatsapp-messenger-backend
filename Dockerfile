# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven files
COPY pom.xml .

# Install Maven
RUN apt-get update && apt-get install -y maven

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Create upload directories
RUN mkdir -p uploads/pictures uploads/videos

# Expose port
EXPOSE 8090

# Run the application
CMD ["java", "-jar", "target/whatsapp-messenger-backend-1.0.0.jar"] 