# Use the official Gradle image to build the project
FROM gradle:8.8-jdk17 AS build

# Set the working directory in the container
WORKDIR /app
ENV GRADLE_USER_HOME /cache

# Copy the Gradle wrapper
COPY gradle gradle
COPY gradlew .
COPY build.gradle settings.gradle /app/
COPY src /app/src
COPY config /app/config

# Ensure Gradle wrapper uses the specific distribution
RUN ./gradlew wrapper --gradle-version 8.8

# Build the project
RUN ./gradlew build -x test

# Use the official OpenJDK image for running the application
FROM amazoncorretto:17.0.7-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/build/libs/user_management-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application runs on
EXPOSE 8085

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
