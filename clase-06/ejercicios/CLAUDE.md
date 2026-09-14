# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Common Commands

- Build and test: `mvn test`
- Run a specific test: `mvn test -Dtest=<TestClassName>`
- Build project: `mvn clean install`
- Start application: `mvn spring-boot:run`

## Architecture & Structure

This project is part of the SIGEO system, specifically focusing on relational persistence using **Spring Boot 4.1** and **Spring Data JPA** with **PostgreSQL**.

### Key Components
- **Domain Models**: Located in `com.sigeo.clase06`, implementing the core entity relationship between `Solicitud`, `Aprobacion`, and `Contacto`.
- **Persistence Layer**: 
  - `SolicitudRepository`: Handles data access.
  - **Migrations**: Database schema evolution is managed by **Flyway**, with migration scripts located in `src/main/resources/db/migration/`.
- **Service Layer**: `SolicitudService` encapsulates business logic.
- **API Layer**: `SolicitudController` exposes REST endpoints for managing requests.
- **DTOs**: `PageDTO` is used for paginated responses.

### Technical Stack
- **Java 25 LTS**
- **Spring Data JPA** (PostgreSQL)
- **Flyway** for migrations
- **JUnit 5**, **AssertJ**, and **Testcontainers** for integration tests
