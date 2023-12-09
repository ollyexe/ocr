#### Stage 1: Build the application
FROM bellsoft/liberica-openjdk-alpine-musl:21 as build

RUN apk update

RUN tesseract --list-langs
RUN tesseract -v

# Set the name of the jar
ENV APP_FILE *.jar


# Copy our JAR
COPY target/$APP_FILE /app.jar

# Launch the Spring Boot application
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
