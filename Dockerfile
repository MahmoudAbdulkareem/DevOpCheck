# Use OpenJDK as the base image
FROM openjdk:17

# Expose the port the application will run on
EXPOSE 8089

# Set environment variables for Nexus credentials
ENV NEXUS_USER=admin
ENV NEXUS_PASSWORD=MahmoodAbdul12

# Download the JAR from the Nexus repository
ADD http://192.168.33.10:8081/repository/maven-releases/tn/esprit/spring/gestion-station-ski/1.0/gestion-station-ski-1.0.jar /gestion-station-ski.jar

# Health check to ensure the application is up and running
HEALTHCHECK --interval=30s --timeout=10s --retries=3 CMD curl -f http://localhost:8089/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "/gestion-station-ski.jar"]
