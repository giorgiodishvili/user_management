FROM gradle:7.3.3-jdk17 AS build

WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY src /app/src

RUN gradle build -x test

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/build/libs/user-management-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
