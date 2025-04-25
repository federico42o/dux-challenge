FROM maven:3.8.1-openjdk-17-slim as build

WORKDIR /app
COPY . /app

RUN mvn clean package


FROM openjdk:17-jdk-alpine

WORKDIR /app
COPY --from=build /app/target/dux-challenge-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]