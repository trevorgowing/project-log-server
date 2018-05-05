[![Build Status](https://travis-ci.org/trevorgowing/project-log-server.svg?branch=master)](https://travis-ci.org/trevorgowing/project-log-server)
[![codecov](https://codecov.io/gh/trevorgowing/project-log-server/branch/master/graph/badge.svg)](https://codecov.io/gh/trevorgowing/project-log-server)
[![Dependency Status](https://www.versioneye.com/user/projects/5ad193c60fb24f3a025bfecc/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/5ad193c60fb24f3a025bfecc)

# Project Log Server

Project Risk and Issue Tracker.

## Technology

### Production Code

* Language: [Java](http://www.oracle.com/technetwork/java/javase/overview/index.html)
* Application Server (Servlet Container): [Apache Tomcat](http://tomcat.apache.org/)
* Application and Web Framework: [Spring Boot](https://projects.spring.io/spring-boot/)
* Code Generation: [Project Lombok](https://projectlombok.org/)

### Test Code

* [Spring Boot Test](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)
* [Rest Assured](http://rest-assured.io/)
* [Test Containers](https://www.testcontainers.org/)

### Tooling

* Build: [Gradle](https://gradle.org/)
* Code Formatting: [Spotless](https://github.com/diffplug/spotless) with [Google Style Guide](https://google.github.io/styleguide/javaguide.html)
* Containerization: [Docker](https://www.docker.com/)

## Build, Run and Test

The recommended [gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) is used to build the application and is included in the source code.

### Build

* With Gradle: `./gradlew build`
* With Docker: `sudo docker build -t trevorgowing/project-log-server:X.X.X .`
* With Docker Compose: `sudo docker-compose build project-log-server`

### Run

* With Gradle: `./gradlew bootrun` (will build if not already built)
* With Docker: `sudo docker run trevorgowing/project-log-server:X.X.X` (needs to be built explicitly)
* With Docker Compose: `sudo docker-compose --build up`
* With Bash Script: `./start.sh` (runs docker-compose internally)

### Test

* With Gradle: `./gradlew test`

## Release

1. Merge feature branch into master
1. Update the version number in build.gradle
1. Update the version number in Dockerfile
1. Commit changes with message `Release X.X.X`
1. Tag release commit with `git tag -a vX.X.X` and include a description `'Brief summary followed by list of significant changes'`
1. Push to master with `git push --follow-tags`

## License

[MIT License](LICENSE)
