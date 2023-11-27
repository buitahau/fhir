FROM amazoncorretto:17
COPY target/tracking-working-time-0.0.1-SNAPSHOT.jar tracking-working-time-0.0.1.jar
ENTRYPOINT ["java","-jar","/tracking-working-time-0.0.1.jar"]