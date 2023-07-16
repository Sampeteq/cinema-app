FROM eclipse-temurin:17
EXPOSE 8080
WORKDIR /app
COPY target/cinema-app-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java","-jar","cinema-app-0.0.1-SNAPSHOT.jar"]