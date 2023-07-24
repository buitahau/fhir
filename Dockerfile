ARG JAVA_VERSION=17
FROM openjdk:${JAVA_VERSION}
COPY target/fhir-0.0.1-SNAPSHOT.jar fhir-docker.jar
EXPOSE 8080
CMD ["java", "-jar", "/fhir-docker.jar"]