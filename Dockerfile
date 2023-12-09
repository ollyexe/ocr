
FROM bellsoft/liberica-openjdk-alpine-musl:21 as build

RUN apk update

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve

COPY src ./src

CMD ["./maven-wrapper", "spring-boot:run"]

# Set the name of the jar
