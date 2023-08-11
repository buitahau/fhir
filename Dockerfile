ARG JAVA_VERSION=17
FROM openjdk:${JAVA_VERSION}
COPY target/fhir-0.0.1-SNAPSHOT.jar fhir-vault.jar
EXPOSE 8081
CMD ["java", "-jar", "/fhir-vault.jar"]