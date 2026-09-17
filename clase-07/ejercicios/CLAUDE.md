# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Common Commands

- **Build and Test All**: `mvn clean test`
- **Run Specific Test**: `mvn test -Dtest=ClassName`
- **Run Specific Test Method**: `mvn test -Dtest=ClassName#methodName`
- **Run Application**: `mvn spring-boot:run` (Requires PostgreSQL running: `docker compose up -d`)
- **Clean Project**: `mvn clean`

## Architecture & Structure

This module is part of the **SIGEO** project, focusing on **Class 07: Transactions, Concurrency, and Testing**.

### High-Level Architecture
The project follows a classic layered architecture:
- **Controller**: REST endpoints for managing `Solicitud` (Requests) and `Aprobacion` (Approvals).
- **Service**: Business logic with a strong focus on `@Transactional` boundaries, propagation, and solving concurrency issues.
- **Repository**: Spring Data JPA interfaces for persistence.
- **Domain**: JPA Entities (`Solicitud`, `Aprobacion`) implementing optimistic locking via `@Version`.

### Key Technical Focuses
- **Transactional Integrity**: Ensuring atomic operations across multiple service calls (Rollback).
- **Concurrency Control**: Implementation of **Optimistic Locking** to prevent "lost updates" in high-concurrency scenarios.
- **Testing Strategy**:
    - **Unit Tests**: Logic testing with minimal mocks (e.g., `AprobacionServiceTest`).
    - **Slice Tests**: Web layer testing using `@WebMvcTest` (e.g., `SolicitudControllerSliceTest`).
    - **Integration Tests**: End-to-end flows using `@SpringBootTest` and **Testcontainers** for a real database (e.g., `SolicitudIntegrationTest`).

## Technical Stack
- **Java 25 LTS**
- **Spring Boot 4.1.x**
- **Spring Data JPA**
- **PostgreSQL** (via Docker Compose) / **H2** (for fast dev)
- **JUnit 5**, **AssertJ**, **Mockito**
