## Spring Boot web application with Spring Framework 6 and Spring Security
### Prerequisites
- Java 21 (JDK)
- Docker (optional): only if you plan to run it inside a container
- MySQL 8 or PostgreSQL
- H2 or SQL Server: if you choose H2, you don't have to install anything, it is bundled as dependency of the project

### Configurations
There are several maven profiles that you need to activate in order for the app to run properly.
- Database profiles: you need to activate **only one** of the following as per the installed RDBMS on your machine
  - `h2-mysql`
  - `h2-pg` (activated by default)
  - `mssql-mysql`
  - `mssql-pg`
- Security profiles: similarly, you must activate **only one** profile among the following
  - `basic-security`
  - `form-login`
  - `oauth2-security`: note that only REST endpoints are accessible up to this date. You will need to run the spring authorization server app alongside as well
- And then you can optionally activate the `docker` maven profile if you wish to use docker for ease of shipping

***TODO: Database creation instructions***

...

### Run the application
**NOTE:** All the commands below must be run from the root folder of the project
#### Using Maven
You can use an installed maven on your local machine :
```
mvn spring-boot:run -P h2-mysql,oauth2-security,-h2-pg -Dspring-boot.run.jvmArguments="-Dmysql.username={your username} -Dmysql.password={your password}"
```
Or use the maven wrapper bundled in this project :
```
./mvnw spring-boot:run -P h2-mysql,oauth2-security,-h2-pg -Dspring-boot.run.jvmArguments="-Dmysql.username={your username} -Dmysql.password={your password}"
```

#### Using the Java command-line tools
If you want to run the webapp using java, you have to package the source code into a jar beforehand:
```
mvn package -P h2-mysql,oauth2-security,-h2-pg -Dmysql.username={your username} -Dmysql.password={your password}
```
This will create an executable jar in the `target` directory.
To run the app, enter:
```
cd target
java -jar spring-boot-webapp-0.0.1-SNAPSHOT.jar --mysql.username={your username} --mysql.password={your password}
```

#### With Docker
The `Dockerfile` is located at `src/main/docker`
To create the image, run:
```
docker build -f ./src/main/docker/Dockerfile -t {image name} .
```
Before you run the container of the web server, you must set up networking
within which the web server and the database containers will interact.
First, create a network:
```
docker network create --driver bridge {network name}
```
(the `--driver bridge` parameter is optional as it's the default)
Now, we have to create all the containers and connect them to that network
to ensure the entire stack run seamlessly
Create the docker container for the MySQL container:
```
docker run -d --name {container name} --network {network name} -e MYSQL_ROOT_PASSWORD={root password} -e MYSQL_DATABASE={database name} -e MYSQL_USER={user} -e MYSQL_PASSWORD={password} mysql
```
If you have enabled OAuth2 authentication (by activating the `oauth2-security` maven profile) while building the app, then you must create a docker container for the authorization server as well:
```
docker run -d -p 9000:9000 --name {container name} --network {network name} {image name}
```
Create the docker container for web server:
```
docker run -d -p 8083:8083 --name {container name} --network {network name} {image name}
```

#### Using Docker Compose
...