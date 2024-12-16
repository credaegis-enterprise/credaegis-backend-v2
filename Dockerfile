FROM openjdk:21-jdk-slim
LABEL authors="allenbastianjoy"
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]