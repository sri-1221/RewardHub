# Use OpenJDK base image
FROM openjdk:17-jdk-slim

# Set working directory inside the container to 'RewardHub'
WORKDIR /rewardhub

# Copy the built JAR file into the container (keep the name 'app.jar' or rename it as per your need)
COPY target/*.jar rewardhub.jar

# Expose the application port
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "rewardhub.jar"]