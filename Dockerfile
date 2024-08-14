# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file into the container
COPY target/owca-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port on which the app will run
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]