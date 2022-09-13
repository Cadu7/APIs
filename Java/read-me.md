# Java API
### Description
This is a simple api using only java without framework, his purpose is just to understand how to create an api with no framework to help us and see the basic concepts of web programming

### How to run
* Running using docker
  * prerequisites
    * Docker
``` 
docker-compose -f ./Docker/docker-compose.yaml up
```
* Normal running
  * prerequisites
    * JDK 11
    * Postgres 14
  * Configure ./src/main/resources/application.properties with your database configurations     
```
mvn clean install package
java -jar ./target/api-java-0.0.1.jar
```

### Endpoints
* GET localhost:8080/api/user
  * QueryParam = userId 
  * type = int
* DELETE localhost:8080/api/user
  * QueryParam = userId 
  * type = int
* POST localhost:8080/api/user
  * Require body
    ```json
    {
      "name":"Carlos Nascimento",
      "password":"123456789"
    }
    ```
* PUT localhost:8080/api/user
  * QueryParam = userId 
  * type = int
    * Require body
    ```json
    {
    "name":"Carlos Nascimento",
    "password":"123456789"
    }
    ```