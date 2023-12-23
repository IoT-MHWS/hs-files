FROM openjdk:17-alpine

COPY "build/libs/hs-files-*.jar" application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]
