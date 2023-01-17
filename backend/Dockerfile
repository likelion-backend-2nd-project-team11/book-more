FROM openjdk:11.0-slim
WORKDIR /app
COPY /build/libs/*-SNAPSHOT.jar ./app.jar

EXPOSE 8080

ENTRYPOINT [                                                \
   "java",                                                 \
   "-jar",                                                 \
   "-Djava.security.egd=file:/dev/./urandom",              \
   "-Dsun.net.inetaddr.ttl=0",                             \
   "app.jar"              \
]
