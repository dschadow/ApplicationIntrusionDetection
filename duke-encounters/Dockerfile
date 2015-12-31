FROM java:8
MAINTAINER Dominik Schadow <dominikschadow@gmail.com>

EXPOSE 8080

COPY duke-encounters/target/*.jar /usr/src/duke-encounters/duke-encounters.jar
WORKDIR /usr/src/duke-encounters
CMD java -Dserver.port=8080 -jar duke-encounters.jar