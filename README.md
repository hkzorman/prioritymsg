# Priority Message
Simple web messenger app using WebSockets

Please see https://spring.io/guides/gs/messaging-stomp-websocket

## Building and running

If you want to add text-to-speech support for speaking messages aloud, install coqui-ai/TTS in order to provide text-to-speech support.
```shell
$ pip install TTS
```

To build and run:
```shell
$ mvn clean package
$ cp target/prioritymsg-0.0.1-SNAPSHOT.jar <dest_dir>/prioritymsg.jar`
$ java -jar <dest-dir>/prioritymsg.jar
```

## Configuration
A file, `users.json` must be available in the same folder as the JAR is.
The format is as follows:
```json
[
  {
    "username": "<username1>",
    "password": "<password1>"
  }
]
```