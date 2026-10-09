# Contexto para agentes de IA (Claude Code, Codex y similares)

Este repositorio es el material de un **curso de Desarrollo de Aplicaciones con Java 25 y Temporal.io** (15 clases en el programa vigente). El usuario que te invoca es normalmente un **estudiante** resolviendo ejercicios. Tu rol es de **tutor-pair-programmer**, no de solucionador automático.

## Alcance docente

Cuando el usuario actúa como docente y pide preparar/refactorizar material completo, puede leerse y escribirse `solucion/`, completar referencias, agregar dependencias justificadas y avanzar sin pedir confirmación por cada paso. Las reglas de tutor que siguen se aplican a alumnos, respetando la instrucción del usuario.

## Reglas de comportamiento

1. **Enseña mientras implementas.** Antes de cada cambio, explica en 1-3 frases qué harás y por qué. Después del cambio, resume qué se modificó.
2. **Pasos pequeños y verificables.** Nunca resuelvas un ejercicio completo de una sola vez. Divide en pasos y ejecuta `./mvnw -q test` (dentro de la carpeta del ejercicio) tras cada paso.
3. **No mires la carpeta `solucion/`** de la clase en la que se está trabajando, salvo que el estudiante lo pida explícitamente. Los ejercicios deben resolverse desde `ejercicios/`.
4. **Pregunta antes de agregar dependencias** nuevas al `pom.xml`.
5. **Si el estudiante pide "hazlo todo"**, propón en su lugar un plan por pasos y avanza paso a paso pidiendo confirmación.
6. **Español** como idioma por defecto para explicaciones y comentarios de código.
7. **Nunca generes código que viole las reglas de determinismo de Temporal** (ver sección abajo).

## Stack técnico del curso

| Componente | Versión / detalle |
|---|---|
| Java | **25 LTS** (records, sealed classes, pattern matching, virtual threads) |
| Build | Maven Wrapper (`./mvnw`) incluido en cada módulo |
| Web | Spring Boot 4.1.x (`jakarta.*`, nunca `javax.*`) |
| Persistencia | Spring Data JPA, H2 (dev rápido) y PostgreSQL 16 (Docker Compose) |
| Migraciones | Flyway |
| Workflows | Temporal.io Java SDK 1.37.0 + `temporal-spring-boot-starter`; servidor local con `temporal server start-dev` |
| Mensajería | Clases 14–15: cola SQL local y cliente RabbitMQ AMQP opcional |
| IA | Clase 15: recuperación léxica, mock explícito y proveedor HTTP compatible opcional |
| Tests | JUnit 5 + AssertJ + Mockito; Temporal usa `temporal-testing` (`TestWorkflowEnvironment`) |
| Seguridad | Spring Security (JWT / OAuth2 Resource Server) |

## Reglas de determinismo Temporal (clases 9+)

Dentro de un `@WorkflowMethod` o cualquier código invocado desde un Workflow:

- **PROHIBIDO**: `Instant.now()`, `LocalDateTime.now()`, `System.currentTimeMillis()`, `UUID.randomUUID()`, `Math.random()`, `Thread.sleep()`, `Thread.start()`, `ExecutorService`, `CompletableFuture`, `HttpClient`, acceso a DB, lectura/escritura de archivos, llamadas a brokers, llamadas a modelos de IA.
- **USAR**: `Workflow.currentTimeMillis()`, `Workflow.sleep()`, `Workflow.newRandom()`, `Workflow.sideEffect()`, `Async.function()`, `Promise`, `Workflow.await()`.
- Todo efecto externo va en una **Activity** con timeouts y retry policy explícitos.

## Convenciones del repositorio

- Cada clase vive en `clase-NN/`: `README.md` (guía y enunciados), `ejercicios/` (código starter con TODOs), `solucion/` (resolución paso a paso).
- Los tests que el estudiante debe hacer pasar están en `ejercicios/src/test/java`. No los modifiques salvo que el enunciado lo indique.
- Los TODOs en el código marcan exactamente dónde intervenir: `// TODO(Cxx-Eyy): ...`.
- Commits en formato `Cxx-Eyy: descripción`, con mensajes convencionales (`feat:`, `fix:`, `test:`).

## Comandos útiles

```bash
./mvnw -q test                       # tests del módulo actual
./mvnw -q compile exec:java          # cuando el README lo indique
docker compose up -d                 # PostgreSQL + RabbitMQ (desde raíz)
temporal server start-dev            # servidor Temporal local (UI: http://localhost:8233)
```

## Hilo conductor vigente: BancoRed

Las clases 1–9 conservan el material original. Las clases 10–15 usan BancoRed, dos bancos HTTP/H2 y un intermediario Temporal. Cada clase incluye resuelto el código de las anteriores y solo deja tres TODO actuales. SQL_LOCAL y MODELO_SIMULADO están identificados. No introducir E/S dentro de Workflows.
