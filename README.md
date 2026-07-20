# Desarrollo de Aplicaciones con Java, Spring Boot y Temporal.io

Repositorio base del curso **Desarrollo de Aplicaciones** (III CRIM — Comunicaciones e Informática, 76 horas, 19 clases de 4 horas). Contiene las prácticas de cada clase, con ejercicios enunciados y su **resolución paso a paso**, diseñadas para ser desarrolladas **con ayuda de Inteligencia Artificial** (modo chat o agentes como Claude Code / Codex).

## Objetivo del curso

Al finalizar el curso, el estudiante será capaz de desarrollar **aplicaciones multicapa resilientes** con Java moderno: APIs web con Spring Boot, persistencia con JPA/PostgreSQL, seguridad, mensajería, flujos de trabajo transaccionales duraderos con [Temporal.io](https://temporal.io/) e integración de Inteligencia Artificial (LLMs) en aplicaciones.

## Estructura del repositorio

| Carpeta | Clase | Tema | Bloque |
|---|---|---|---|
| `clase-01` | 1 | Java moderno, POO y trabajo con IA | 1. Fundamentos |
| `clase-02` | 2 | Excepciones, logging, clases internas, GC | 1. Fundamentos |
| `clase-03` | 3 | Depuración, pruebas (JUnit 5) y JavaDoc | 1. Fundamentos |
| `clase-04` | 4 | Colecciones, Streams y concurrencia | 1. Fundamentos |
| `clase-05` | 5 | Spring Boot, IoC/DI y API REST | 2. Web y datos |
| `clase-06` | 6 | Persistencia con Spring Data JPA + PostgreSQL | 2. Web y datos |
| `clase-07` | 7 | JPQL, Criteria, paginación, transacciones y locking | 2. Web y datos |
| `clase-08` | 8 | Seguridad web con Spring Security y JWT | 2. Web y datos |
| `clase-09` | 9 | Temporal.io: primer workflow duradero | 3. Resiliencia |
| `clase-10` | 10 | Activities, timeouts y políticas de reintento | 3. Resiliencia |
| `clase-11` | 11 | Señales, queries y timers duraderos | 3. Resiliencia |
| `clase-12` | 12 | Patrón Saga y Temporal + Spring Boot | 3. Resiliencia |
| `clase-13` | 13 | IA integrada I: LLMs desde Java (chat, salida estructurada) | 4. IA |
| `clase-14` | 14 | IA integrada II: RAG y function calling | 4. IA |
| `clase-15` | 15 | Mensajería asíncrona, Bean Validation y AOP | 4. IA / soporte |
| `clase-16` | 16 | Proyecto integrador: diseño y arquitectura | 5. Integración |
| `clase-17` | 17 | Proyecto integrador: implementación (hackathon) | 5. Integración |
| `clase-18-examen` | 18 | Examen final y defensa de proyectos | 5. Integración |
| `clase-19-repeticion` | 19 | Examen de repetición y retroalimentación | 5. Integración |

Cada carpeta `clase-NN` contiene:

- `README.md` — guía de la clase: objetivos, contenidos, cronograma de 4 horas y ejercicios enunciados.
- `ejercicios/` — código base (starter) para trabajar los ejercicios.
- `solucion/` — resolución **paso a paso** con código final comentado.

## Requisitos de entorno

| Herramienta | Versión | Verificación |
|---|---|---|
| JDK | 21 (LTS) | `java --version` |
| Maven | 3.9+ | `mvn --version` |
| Docker + Docker Compose | reciente | `docker --version` |
| Temporal CLI | última | `temporal --version` |
| Git | 2.40+ | `git --version` |
| IDE | IntelliJ IDEA CE o VS Code | — |
| Agente de IA | Claude Code, Codex u otro | — |

Instrucciones detalladas de instalación en [`docs/SETUP.md`](docs/SETUP.md).

## Cómo trabajar con IA en este curso

Este curso asume que **siempre tendrás un asistente de IA disponible**. Las reglas del juego:

1. **Primero entiende, después acepta.** Nunca integres código que no puedas explicar línea por línea.
2. **Da contexto en tus prompts.** Incluye versión de Java, framework, el error completo y lo que ya intentaste.
3. **Pide explicaciones, no solo soluciones.** "Explícame por qué falla" produce más aprendizaje que "arréglalo".
4. **Verifica con pruebas.** Cada ejercicio incluye tests (`mvn test`); el código de la IA debe pasarlos.
5. **Revisa el diff.** Cuando uses un agente (Claude Code/Codex), revisa cada cambio antes de hacer commit.

La guía completa de prompts y flujo de trabajo con agentes está en [`docs/GUIA_IA.md`](docs/GUIA_IA.md). Los archivos [`AGENTS.md`](AGENTS.md) y [`CLAUDE.md`](CLAUDE.md) del repositorio configuran el contexto para los agentes de código.

## Evaluación

| Instancia | Ponderación | Modalidad |
|---|---|---|
| Evaluación sumativa 1 (fin Bloque 1-2, clase 8) | 20% | Práctica grupal/individual |
| Evaluación sumativa 2 (fin Bloque 3, clase 12) | 20% | Práctica individual |
| Tercera instancia (proyecto integrador, clase 17) | 30% | Individual |
| Examen de unidad (clase 18) | 30% | Individual |

Evaluación formativa permanente: quizzes de 15 minutos al inicio de cada clase, guías y laboratorios.

## Documentación

- [`docs/PLANIFICACION.md`](docs/PLANIFICACION.md) — planificación detallada de las 19 clases.
- [`docs/SETUP.md`](docs/SETUP.md) — instalación del entorno paso a paso.
- [`docs/GUIA_IA.md`](docs/GUIA_IA.md) — metodología de trabajo con asistentes de IA.
- [`docs/RUBRICAS.md`](docs/RUBRICAS.md) — rúbricas de evaluación.
