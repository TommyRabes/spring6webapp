FROM openjdk:21-jdk-slim

ENV JAVA_OPTS " -Xms512m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

WORKDIR application

COPY ../../../target/spring-boot-webapp-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["java", "-jar", "spring-boot-webapp-0.0.1-SNAPSHOT.jar", "--mysql.username=Tommy", "--mysql.password=123456"]