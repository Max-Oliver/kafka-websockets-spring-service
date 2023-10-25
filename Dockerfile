FROM openjdk:17

VOLUME /tmp
COPY build/libs/ws-java-poc-v1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
