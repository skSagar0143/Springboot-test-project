# Build the application JAR first with: mvn clean package
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/springboot-test-project-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]