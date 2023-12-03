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
ENV APP_FILE *.jar


# Copy our JAR
COPY target/$APP_FILE /app.jar

# Launch the Spring Boot application
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
