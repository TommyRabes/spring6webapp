FROM openjdk:21-jdk-slim

ENV JAVA_OPTS " -Xms512m -Xmx512m -Djava.security.egd=file:/dev/./urandom"
ENV mysql.username Tommy
ENV mysql.password 123456

WORKDIR application

COPY maven/${project.build.finalName}.jar ./

ENTRYPOINT ["java", "-jar", "${project.build.finalName}.jar"]