FROM openjdk:14-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY target/*.jar home/spring/application.jar
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "/home/spring/application.jar"]
