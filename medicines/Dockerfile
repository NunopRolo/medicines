FROM gradle AS build
RUN mkdir -p /medicines-backend
WORKDIR /medicines-backend

COPY . /medicines-backend

RUN ./gradlew clean bootJar

FROM openjdk:17
COPY --from=build /medicines-backend/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
