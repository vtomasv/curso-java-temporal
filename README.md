# Curso: Desarrollo de Aplicaciones con Java 25 y Temporal.io

Repositorio de ejercicios del curso **Desarrollo de Aplicaciones** (76 horas, 19 clases de 4 horas). El curso construye de forma incremental **SIGEO — Sistema Integrado de Gestión de Solicitudes y Operaciones**: desde una aplicación de consola hasta una solución web segura con persistencia, Workflows durables con Temporal.io, colas de mensajes e integración de IA.

> Este repositorio contiene **solamente los fuentes de los ejercicios** de cada clase. Está pensado para trabajar con **Visual Studio Code** y **Docker**.

## Línea tecnológica

| Componente | Versión / detalle |
|---|---|
| Java | **JDK 25 LTS** (records, sealed classes, pattern matching, virtual threads) |
| Build | Maven 3.9+ con Maven Wrapper (`./mvnw`) |
| Web | **Spring Boot 4.1.x** (`jakarta.*`, nunca `javax.*`) |
| Persistencia | Spring Data JPA + Hibernate, **PostgreSQL 16** (Docker), Flyway, H2 solo para pruebas rápidas |
| Workflows | **Temporal.io Java SDK 1.37.0** + `temporal-spring-boot-starter`; servidor local con `temporal server start-dev` |
| Mensajería | **RabbitMQ** + Spring AMQP (Kafka/JMS se tratan comparativamente) |
| IA | **Spring AI** con proveedor compatible OpenAI (`OPENAI_API_KEY` externalizada) |
| Seguridad | Spring Security (JWT / OAuth2 Resource Server) |
| Pruebas | JUnit 5 + AssertJ + Mockito; Testcontainers; `temporal-testing` (`TestWorkflowEnvironment`, time skipping, replay) |
| Entorno | **VS Code** (Extension Pack for Java, Spring Boot Extension Pack, Docker, REST Client, Git) |

## Estructura del repositorio

```text
curso-java-temporal/
├── README.md
├── compose.yaml                    # PostgreSQL, RabbitMQ y servicios stub
├── docs/                           # setup, planificación, guía de IA, rúbricas
├── clase-01/                       # Java 25 LTS, ecosistema y entorno profesional
│   ├── README.md                   # objetivos, cronograma, ejercicios E01–E08 y tareas T01–T04
│   ├── ejercicios/                 # código starter con TODOs numerados y pruebas públicas
│   └── solucion/                   # resolución paso a paso (uso docente)
├── clase-02/                       # POO moderna y diseño mantenible
├── ...
├── clase-17/                       # Proyecto integrador: hardening y defensa
├── clase-18/                       # Examen final (starter neutral, sin soluciones)
└── clase-19/                       # Examen de repetición y cierre técnico
```

Cada carpeta `ejercicios/` compila antes de comenzar; los `TODO(Cxx-Eyy)` están numerados y no incluyen la solución. Las pruebas públicas en `src/test/java` definen el contrato que el alumno debe satisfacer.

## Mapa del curso (19 clases)

| Clase | Bloque | Tema |
|---|---|---|
| 01 | B1 Fundamentos | Java 25 LTS, ecosistema Java y entorno profesional |
| 02 | B1 Fundamentos | POO moderna: records, sealed classes, SOLID |
| 03 | B1 Fundamentos | Excepciones, logging, depuración y gestión de memoria |
| 04 | B1 Fundamentos | Collections, Streams, E/S, HTTP, concurrencia y pruebas |
| 05 | B2 Web y persistencia | Spring Boot 4: DI, REST y Thymeleaf |
| 06 | B2 Web y persistencia | Spring Data JPA, PostgreSQL y Flyway |
| 07 | B2 Web y persistencia | Transacciones, concurrencia, pruebas por capas |
| 08 | B2 Web y persistencia | Seguridad web con Spring Security y JWT |
| 09 | B3 Workflows resilientes | Temporal.io: arquitectura y ejecución duradera |
| 10 | B3 Workflows resilientes | Activities: timeouts, retries, heartbeats, idempotencia |
| 11 | B3 Workflows resilientes | Signals, Queries, Updates, timers y Continue-As-New |
| 12 | B3 Workflows resilientes | Microservicios, transacciones distribuidas y Saga |
| 13 | B3 Workflows resilientes | Pruebas Temporal, replay, versionado y observabilidad |
| 14 | B4 IA y avanzadas | IA en aplicaciones Java y agentes durables |
| 15 | B4 IA y avanzadas | Middleware de mensajes, colas y procesamiento asíncrono |
| 16 | B5 Integración final | Proyecto integrador: requisitos y arquitectura |
| 17 | B5 Integración final | Proyecto integrador: hardening y ensayo de defensa |
| 18 | B5 Evaluación | Examen final teórico-práctico y defensa |
| 19 | B5 Evaluación | Examen de repetición, nivelación y cierre |

## Puesta en marcha rápida

```bash
# 1. Verificar herramientas
java --version        # JDK 25 LTS
docker --version
git --version

# 2. Levantar infraestructura (desde la raíz del repositorio)
docker compose up -d  # PostgreSQL 16 + RabbitMQ

# 3. Servidor Temporal local (clases 9+)
temporal server start-dev   # UI en http://localhost:8233

# 4. Trabajar un ejercicio
cd clase-01/ejercicios
./mvnw test           # las pruebas públicas guían el contrato
```

## Reglas obligatorias de diseño Temporal (clases 9–19)

1. El Workflow contiene **orquestación determinista**; toda llamada HTTP, base de datos, archivo, broker, modelo de IA o efecto externo se implementa como **Activity**.
2. Dentro de Workflow usar `Workflow.currentTimeMillis()`, `Workflow.sleep()`, `Async`/`Promise` y utilidades de Temporal; **prohibido** `Instant.now()`, `UUID.randomUUID()`, `Thread`, `ExecutorService` o `CompletableFuture` nativos.
3. Toda Activity declara **timeouts**; toda política de retry tiene justificación, límite y clasificación de errores no reintentables.
4. Las Activities con efectos son **idempotentes** (claves de negocio, restricciones únicas, inbox/deduplicación).
5. Las Activities largas emiten **heartbeats** y reanudan progreso; la cancelación es cooperativa.
6. Workflow IDs **estables y de negocio**; `commandId`/`messageId` para deduplicar Signals/Updates/eventos.
7. Event History acotada: payloads pequeños, referencias por ID, Continue-As-New cuando corresponda.
8. Cambios de Workflow pasan **replay testing** y estrategia de versionado antes del despliegue.
9. Preferir **cancelación** sobre terminación.
10. Mayoría de pruebas Temporal de integración con `TestWorkflowEnvironment` y time skipping.

## Convención de entregas

Cada ejercicio se entrega en un commit con formato `Cxx-Eyy: descripción`; las tareas usan `Cxx-Tyy`. Ejemplo: `C05-E03: POST /api/solicitudes con DTO y 201 Location`.

## Definition of Done para todo ejercicio

- Compila y ejecuta con Maven Wrapper desde terminal (`./mvnw verify`).
- Las pruebas relevantes pasan y el alumno puede explicar qué verifican.
- No hay secretos ni datos sensibles en código, logs, fixtures ni historia de Git.
- Errores manejados con mensajes y contexto útiles; sin `catch` vacíos.
- README con comandos exactos de ejecución.
- En Temporal: determinismo, timeouts, retry limitado e idempotencia.
- El alumno puede defender el código sin depender de la respuesta de una IA.

## Cómo trabajar con IA en este curso

Este curso asume que siempre tendrás un asistente de IA disponible. Las reglas del juego:

1. **Primero entiende, después acepta.** Nunca integres código que no puedas explicar línea por línea.
2. **Da contexto en tus prompts.** Incluye versión de Java, framework, el error completo y lo que ya intentaste.
3. **Pide explicaciones, no solo soluciones.**
4. **Verifica con pruebas.** El código de la IA debe pasar `./mvnw test`.
5. **Revisa el diff** antes de cada commit y registra el prompt utilizado.

Guía completa en [`docs/GUIA_IA.md`](docs/GUIA_IA.md). Los archivos [`AGENTS.md`](AGENTS.md) y [`CLAUDE.md`](CLAUDE.md) configuran el contexto para agentes de código.

## Evaluación

| Componente | Peso | Evidencia |
|---|---:|---|
| Ejercicios y evidencias de clase | 25% | Commits, pruebas, ticket de salida y explicación oral |
| Tareas y bitácora técnica | 20% | Calidad, reproducibilidad, revisión crítica de IA y puntualidad |
| Controles prácticos por bloque | 15% | Katas, debugging, seguridad y Temporal |
| Proyecto integrador | 25% | Arquitectura, funcionalidad, resiliencia, seguridad, pruebas y defensa |
| Examen final individual | 15% | Teórico-práctico y capacidad de diagnóstico |

## Documentación

- [`docs/PLANIFICACION.md`](docs/PLANIFICACION.md) — planificación detallada de las 19 clases.
- [`docs/SETUP.md`](docs/SETUP.md) — instalación del entorno paso a paso (VS Code + Docker).
- [`docs/GUIA_IA.md`](docs/GUIA_IA.md) — metodología de trabajo con asistentes de IA.
- [`docs/RUBRICAS.md`](docs/RUBRICAS.md) — rúbrica transversal de evaluación.
