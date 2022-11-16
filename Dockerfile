FROM amazoncorretto:11-alpine
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
EXPOSE 8083
ENTRYPOINT ["./mvnw", "spring-boot:run"]
