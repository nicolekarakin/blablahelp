FROM openjdk:18

ENV ENVIRONMENT=dev

LABEL maintainer="nnn4eu"

ADD blablahelp-backend/target/blablahelp.jar blablahelp.jar

CMD [ "sh", "-c", "java -Dserver.port=$PORT -jar /blablahelp.jar" ]