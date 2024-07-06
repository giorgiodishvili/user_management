# User Management Service

This project is a Spring Boot application for managing user authentication and authorization. It includes features for user registration, login, and role-based access control.

## Project Structure

- **build.gradle**: Gradle build file.
- **Dockerfile**: Docker configuration file for containerizing the application.
- **gradlew / gradlew.bat**: Gradle wrapper scripts.
- **settings.gradle**: Gradle settings file.

### Source Files
- **src/main/java/com/gv/user/management**: Main application code.
    - **auth**: Authentication classes and services.
    - **controller**: REST controllers.
    - **dto**: Data Transfer Objects.
    - **exceptions**: Custom exception handling.
    - **facade**: Service facade layer.
    - **mapper**: Entity-DTO mappers.
    - **messaging**: Event producers.
    - **model**: Entity classes.
    - **service**: Service layer.
    - **annotations**: Custom annotations.
    - **config**: Configuration classes.
    - **constant**: Constants.

### Resources
- **src/main/resources**: Configuration files.
    - **application.yml**: Application configuration.
    - **db/migration**: Database migration scripts.

## Running the Application

To run the application, use the following commands:

```bash
./gradlew bootRun
```

### Using Docker Compose

To run the application using Docker Compose, follow these steps:

1. Clone the configuration repository:

   ```bash
   git clone https://github.com/giorgiodishvili/gv_configuration.git
   cd gv_configuration
2. run for SHELL
```bash
./build.sh

```
 run for WINDOWS
```bat
./build.bat

```

### Access swagger on: 
http://localhost:8080/swagger-ui/index.html

Endpoint for Registration: \
http://localhost:8080/swagger-ui/index.html#/authentication-controller/register

#### email should be valid 
e.g. example@gmail.com
#### password password should be valid uppercase, lowercase, a number and special char 
e.g. Password123!

#### Pass returned access_token to AUTHORIZATION header
