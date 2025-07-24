FROM azul/zulu-openjdk-alpine:17-jre as builder
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract
ENTRYPOINT ["java", "-jar", "application.jar"]