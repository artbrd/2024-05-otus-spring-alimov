FROM maven:3.8.5-openjdk-17 AS builder
COPY src /usr/app/src
COPY pom.xml /usr/app
RUN mvn -f /usr/app/pom.xml clean package

FROM bellsoft/liberica-openjdk-alpine-musl:21.0.1
COPY --from=builder /usr/app/target/spring-docker-demo.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]