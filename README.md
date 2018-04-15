Application Intrusion Detection
============
This repository is all about Application Intrusion Detection. Have a look at my 
[presentations](https://blog.dominikschadow.de/events) I've given on this topic or watch the recordings at 
[W-JAX 2015](https://jaxenter.de/web-app-security-43952) (German) or [JavaZone 2016](https://vimeo.com/181788148) 
(English).

# Duke Encounters
**Duke Encounters** is a demo web application utilizing Application Intrusion Detection based on 
[OWASP AppSensor](http://appsensor.org). It is using [Spring Boot](http://projects.spring.io/spring-boot) with a 
[h2](http://www.h2database.com) in-memory database and a [Thymeleaf](http://www.thymeleaf.org) UI. Keep in mind that 
all entered information is only stored temporarily and will be reset when restarting. 

## Running
No matter what type you choose, the web application should be available on **http://localhost:8080**. Any modern browser
will work. Most parts of the application require a logged in user. You can either register a new one or use one of the 
available users listed in the [src/main/resources/data.sql](https://github.com/dschadow/ApplicationIntrusionDetection/blob/master/duke-encounters/src/main/resources/data.sql) file. Username 
and password are always identical.

### Maven Build
You can run the application with **mvn spring-boot:run**.

### Boxfuse Build 
As an alternative, you can use [Boxfuse](https://boxfuse.com) to fuse, launch the app within an immutable image.

### Docker Build
And you can use [Docker](https://www.docker.com). You can either build the image yourself or pull one from Docker Hub.
To build it yourself you have to set a new version for the application via `mvn versions:set` in the repository root 
directory (ApplicationIntrusionDetection). Enter (almost) any version number you like. Create the Docker image with 
`mvn package docker:build` and launch the container via `docker container run -d -p 8080:8080 -t dschadow/duke-encounters`. 

To pull the latest existing image use `docker pull dschadow/duke-encounters`. Other versions are available on 
[Docker Hub](https://hub.docker.com/r/dschadow/duke-encounters/).

## Meta
[![Build Status](https://travis-ci.org/dschadow/ApplicationIntrusionDetection.svg)](https://travis-ci.org/dschadow/ApplicationIntrusionDetection)
[![Code Climate](https://codeclimate.com/github/dschadow/ApplicationIntrusionDetection/badges/gpa.svg)](https://codeclimate.com/github/dschadow/ApplicationIntrusionDetection)
[![codecov](https://codecov.io/gh/dschadow/ApplicationIntrusionDetection/branch/develop/graph/badge.svg)](https://codecov.io/gh/dschadow/ApplicationIntrusionDetection)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
