FROM eclipse-temurin:21-alpine

RUN apk -U upgrade --ignore alpine-baselayout

COPY jarpack/*.jar /app.jar

EXPOSE 8080

ENV JAVA_OPTS=""
ENV APPLICATION_OPTS=""

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar $APPLICATION_OPTS" ]
