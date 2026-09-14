# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Test Commands
- **Build project**: `./mvn clean compile`
- **Run all tests**: `./mvn test`
- **Run a specific test**: `./mvn test -Dtest=ClassName`
- **Run application**: `./mvn spring-boot:run`

## Architecture and Structure
This module is a Spring Boot 4.1 application for the SIGEO system, focusing on request (solicitud) management.

### High-Level Architecture
- **Layered Pattern**:
    - **Controllers**: Handle HTTP requests. 
        - `SolicitudController`: REST API for JSON communication.
        - `SolicitudWebController`: MVC Controller for Thymeleaf HTML views.
    - **Service**: `SolicitudService` contains business logic and coordinates between controllers and repositories.
    - **Repository**: `SolicitudRepository` defines the data access contract. 
        - `InMemorySolicitudRepository`: Current in-memory implementation.
- **DTOs**: Used for data transfer between API and client (`CrearSolicitudDto`, `SolicitudResponseDto`).
- **Error Handling**: `GlobalExceptionHandler` manages application-wide exceptions and returns consistent error responses.
- **Observability**: Integration with `spring-boot-starter-actuator` via `HealthController` and `ActuatorTest`.

### Project Structure
- `src/main/java/com/sigeo/clase05`: Main application code.
- `src/main/resources/templates`: Thymeleaf HTML templates (`formulario.html`, `listado.html`).
- `src/test/java/com/sigeo/clase05`: JUnit 5 tests for services, controllers and actuator.
