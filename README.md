Application Intrusion Detection
============
This repository is all about Application Intrusion Detection. Have a look at my [blog](https://blog.dominikschadow.de/events) for presentations I've given on this topic or watch the (German) [recording](https://jaxenter.de/web-app-security-43952) at W-JAX 2015.

#duke-encounters
**Duke Encounters** is a complete web application utilizing Application Intrusion Detection based on [OWASP AppSensor](http://appsensor.org). 
This web application is using [Spring Boot](http://projects.spring.io/spring-boot) with a [h2](http://www.h2database.com) in-memory database 
and offers a [Thymeleaf](http://www.thymeleaf.org) UI. Keep in mind that all entered information is only stored temporarily and will be lost 
when restarting. Run this application with **mvn spring-boot:run**. After launching, open the web application in your browser at **http://localhost:8080**.

As an alternative, you can use [Boxfuse](https://boxfuse.com) to fuse, launch the app within an immutable image and open the web application 
in your browser at **http://127.0.0.1:8080**.

And of course you can use [Docker](https://www.docker.com) to create an image via `docker build -t duke-encounters .`, launch the container 
via `docker run -d -i -p 8080:8080 -t duke-encounters` and open the web application in your browser at **http://[CONTAINER-IP]:8080** (you 
can find out your container IP by executing `docker-machine ip`.

Available users are listed in the [src/main/resources/data.sql](https://github.com/dschadow/ApplicationIntrusionDetection/blob/master/duke-encounters/src/main/resources/data.sql) 
file. Username and password are always identical.

The GitHub login requires the setup of an application in your GitHub account and to provide the valid `github.client.clientId` and `github.client.clientSecret` 
as runtime parameters.

##Meta
[![Build Status](https://travis-ci.org/dschadow/ApplicationIntrusionDetection.svg)](https://travis-ci.org/dschadow/ApplicationIntrusionDetection)
[![Code Climate](https://codeclimate.com/github/dschadow/ApplicationIntrusionDetection/badges/gpa.svg)](https://codeclimate.com/github/dschadow/ApplicationIntrusionDetection)
[![codecov](https://codecov.io/gh/dschadow/ApplicationIntrusionDetection/branch/master/graph/badge.svg)](https://codecov.io/gh/dschadow/ApplicationIntrusionDetection)
