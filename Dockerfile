# Stage 1: Build the application using Maven
# Change to a base image with JDK 21
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final, smaller runtime image
# Change to a base image with JDK 21
FROM maven:3.9-eclipse-temurin-21
WORKDIR /app
# Copy the built JAR file from the 'build' stage
COPY --from=build /app/target/*.jar app.jar
# Expose the port the application runs on
EXPOSE 8091
# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
