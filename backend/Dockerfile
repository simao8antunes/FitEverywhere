# Use a base image with Maven to build the JAR
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory for the build
WORKDIR /app

# Copy the pom.xml and source code to the container
COPY pom.xml ./
COPY src ./src

# Package the application to generate the JAR
RUN mvn clean package -DskipTests

# Use a base image with JDK to run the application
FROM build

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
