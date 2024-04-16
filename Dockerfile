FROM eclipse-temurin:22
RUN apt-get update
RUN apt-get -y install wget build-essential libssl-dev ca-certificates libasound2 maven
RUN wget http://archive.ubuntu.com/ubuntu/pool/main/o/openssl/libssl1.1_1.1.0g-2ubuntu4_amd64.deb
RUN dpkg -i libssl1.1_1.1.0g-2ubuntu4_amd64.deb
ENV SPEECH_REGION=eastus
ENV HOST=0.0.0.0
VOLUME /tmp
ARG JAR_FILE
ARG USERS_FILE
COPY ${JAR_FILE} app.jar
COPY ${USERS_FILE} users.json
COPY docker-azure-sdk-pom.xml .
RUN mvn clean dependency:copy-dependencies -f docker-azure-sdk-pom.xml
RUN mkdir -p /static/audio
COPY sound.wav /static/audio/sound.wav
ENTRYPOINT ["java","-jar","/app.jar"]