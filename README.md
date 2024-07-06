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
