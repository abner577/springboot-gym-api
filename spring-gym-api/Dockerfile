# Use official Java 21 base image
FROM openjdk:21

# Create a volume for temporary files
VOLUME /tmp

# Copy the built jar from the target directory to the container
COPY target/*.jar app.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app.jar"]