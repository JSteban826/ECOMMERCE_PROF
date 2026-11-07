FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build
COPY pom.xml .
COPY domain domain
COPY application application
COPY boot boot
RUN mvn -B clean verify
FROM eclipse-temurin:25-jre
WORKDIR /app
RUN mkdir -p /app && chown 10001:10001 /app
COPY --from=build --chown=10001:10001 /build/boot/target/notificaciones-boot-1.0.0-SNAPSHOT.jar app.jar
USER 10001:10001
EXPOSE 8083
ENTRYPOINT ["java","-jar","/app/app.jar"]
