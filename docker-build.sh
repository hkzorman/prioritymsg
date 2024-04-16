#:/bin/bash
docker remove kat98webapp
docker build --build-arg JAR_FILE=target/prioritymsg-1.0.0.jar --build-arg USERS_FILE=users.json -t kat98webapp:1.0.0 .
docker run -p 8080:8080 -e SPEECH_KEY=e84cfec0a57c4e11bf9bfaa3581480aa -e BASE_PATH=/static/audio --name kat98webapp kat98webapp:1.0.0