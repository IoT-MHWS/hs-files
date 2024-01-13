FROM openjdk:17-alpine

RUN apk add curl

COPY "build/libs/hs-files-*.jar" application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]
