# Use OpenJDK as the base image
FROM openjdk:17

EXPOSE 8089

ENV NEXUS_USER=admin
ENV NEXUS_PASSWORD=admin123

ADD http://192.168.33.10:8081/repository/maven-releases/tn/esprit/spring/gestion-station-ski/1.0/gestion-station-ski-1.0.jar /gestion-station-ski.jar

HEALTHCHECK --interval=30s --timeout=10s --retries=3 CMD curl -f http://localhost:8089/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/gestion-station-ski.jar"]
