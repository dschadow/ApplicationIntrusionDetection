FROM java:8
MAINTAINER Dominik Schadow <dominikschadow@gmail.com>

VOLUME /tmp
EXPOSE 8080

ENV USER_NAME duke
ENV APP_HOME /home/$USER_NAME/app

RUN useradd -ms /bin/bash $USER_NAME
RUN mkdir $APP_HOME

COPY target/*.jar $APP_HOME/duke-encounters.jar
RUN chown $USER_NAME $APP_HOME/duke-encounters.jar

USER $USER_NAME
WORKDIR $APP_HOME
RUN bash -c 'touch duke-encounters.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","duke-encounters.jar"]