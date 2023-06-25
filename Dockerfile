FROM amazoncorretto:20-alpine-jdk

COPY target/Shapyfy-1.0.0.jar shapyfy.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=local", "/shapyfy.jar"]
