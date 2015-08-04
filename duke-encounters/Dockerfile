FROM java:8
MAINTAINER Dominik Schadow

VOLUME /tmp

ADD target/duke-encounters.jar app.jar

RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]