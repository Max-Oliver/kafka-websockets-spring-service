FROM openjdk:17
VOLUME /tmp
COPY build/libs/ws-java-poc-v1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]


#ARG JAR_FILE
#COPY ${JAR_FILE} app.jar
