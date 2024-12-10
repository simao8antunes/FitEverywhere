FROM maven:3.8-openjdk-18 AS build
ARG SPRING_PROFILES_ACTIVE
ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE
WORKDIR /app
COPY src ./src
COPY pom.xml .
COPY checkstyle.xml .
RUN mvn clean package -DskipTests

FROM build
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
