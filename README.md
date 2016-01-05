Application Intrusion Detection
============
This repository is all about Application Intrusion Detection.

#duke-encounters
**Duke Encounters** is a complete web application utilizing Application Intrusion Detection based on [OWASP AppSensor](http://appsensor.org). This web application is using [Spring Boot](http://projects.spring.io/spring-boot) with a [h2](http://www.h2database.com) in-memory database and offers a [Thymeleaf](http://www.thymeleaf.org) UI. Keep in mind that all entered information is only stored temporarily and will be lost when restarting. Run this application with **mvn spring-boot:run**. As an alternative, you can use [Boxfuse](https://boxfuse.com) to fuse and launch the app within an immutable image. After launching, open the web application in your browser at **http://localhost:8080**.

Available users are listed in the [src/main/resources/import.sql](https://github.com/dschadow/ApplicationIntrusionDetection/blob/master/duke-encounters/src/main/resources/import.sql) file. Username and password are always identical.

##Meta
[![Build Status](https://travis-ci.org/dschadow/ApplicationIntrusionDetection.svg)](https://travis-ci.org/dschadow/ApplicationIntrusionDetection)