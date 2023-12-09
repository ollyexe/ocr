#### Stage 1: Build the application
FROM bellsoft/liberica-openjdk-alpine-musl:21 as build

RUN apk update

# Install tesseract library
RUN apk add --no-cache tesseract-ocr tesseract-ocr-data-deu tesseract-ocr-data-ita tesseract-ocr-data-eng tesseract-ocr-data-ara
ENV LD_LIBRARY_PATH=/tesseract/lib
# Download last language package
RUN mkdir -p /tessdata
ADD https://raw.githubusercontent.com/tesseract-ocr/tessdata/main/eng.traineddata /tessdata/eng.traineddata
ADD https://raw.githubusercontent.com/tesseract-ocr/tessdata/main/ita.traineddata /tessdata/ita.traineddata
ADD https://raw.githubusercontent.com/tesseract-ocr/tessdata/main/deu.traineddata /tessdata/deu.traineddata
ADD https://raw.githubusercontent.com/tesseract-ocr/tessdata/main/ara.traineddata /tessdata/ara.traineddata

# Check the installation status
RUN tesseract --list-langs
RUN tesseract -v

# Set the name of the jar
# common for all images
ENV MAVEN_HOME /usr/share/maven

COPY --from=maven:3.9.5-eclipse-temurin-11 ${MAVEN_HOME} ${MAVEN_HOME}
COPY --from=maven:3.9.5-eclipse-temurin-11 /usr/local/bin/mvn-entrypoint.sh /usr/local/bin/mvn-entrypoint.sh
COPY --from=maven:3.9.5-eclipse-temurin-11 /usr/share/maven/ref/settings-docker.xml /usr/share/maven/ref/settings-docker.xml

RUN ln -s ${MAVEN_HOME}/bin/mvn /usr/bin/mvn

ARG MAVEN_VERSION=3.9.5
ARG USER_HOME_DIR="/root"
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

ENTRYPOINT ["/usr/local/bin/mvn-entrypoint.sh"]
COPY src ./src
COPY pom.xml .
RUN mvn clean install



# Launch the Spring Boot application
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar ./target/ocr-0.0.1-SNAPSHOT.jar" ]
