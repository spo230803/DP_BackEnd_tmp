# build
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

# run
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8443
#ENTRYPOINT ["java","-jar","/app/app.jar"]
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=prod"]
