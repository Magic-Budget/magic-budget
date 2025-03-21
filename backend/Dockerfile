# Stage 1: Build the application
FROM openjdk:23-jdk-slim AS builder
# Set the working directory inside the container
WORKDIR /app

# Copy Gradle wrapper and Kotlin DSL configuration files
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle /app/gradle

# Grant executable permission to the Gradle wrapper
RUN chmod +x gradlew

# Cache dependencies by downloading them
RUN ./gradlew build --no-daemon -x test || return 0

# Copy the rest of the application files
COPY . .

# Build the application, skipping tests
RUN ./gradlew clean build --no-daemon -x test

# Stage 2: Run the application
FROM openjdk:23-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the application's port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
