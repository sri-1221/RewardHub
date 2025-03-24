# Stage 1: Build the application
FROM maven:3.8.1-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and download the dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy the rest of the application source code
COPY src ./src

# Build the application (this will create the .jar file)
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

# Set the working directory in the runtime container
WORKDIR /app

# Copy the built .jar file from the build stage
COPY --from=build /app/target/*.jar rewardhub.jar

# Expose the port the app will run on (optional, depending on your app)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "rewardhub.jar"]
