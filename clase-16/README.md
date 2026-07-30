# Clase 16: Proyecto integrador: requisitos, arquitectura y plan de construcción

**Bloque:** Bloque 5 — Integración final
**Duración:** 4 horas

## Objetivos de Aprendizaje
- Definir alcance mínimo y extensiones sin convertir el proyecto en una colección de tecnologías.
- Modelar dominio, API, esquema, Workflow/Saga, mensajes y amenazas.
- Definir pruebas, SLO, observabilidad y estrategia de despliegue local.
- Descomponer en issues verticales y asignar responsabilidades.
- Producir un walking skeleton ejecutable al final de la sesión.

## Cronograma de la Clase

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–15 | Presentación del caso y rúbrica | Aclarar mínimo obligatorio y extensiones. |
| 15–40 | Taller de requisitos | Equipos escriben historias y criterios. |
| 40–65 | Diseño C4/dominio | Revisión docente por mesa. |
| 65–85 | Ejercicios E01–E03 | Arquitectura, datos y Workflow. |
| 85–100 | Receso | Revisar riesgos de alcance. |
| 100–125 | Seguridad, pruebas y observabilidad | Completar threat model y test strategy. |
| 125–165 | Laboratorio E04–E06 | Backlog y walking skeleton. |
| 165–185 | Desafíos E07–E08 | ADR y demo de inicio end-to-end. |
| 185–195 | Gate de salida | Aprobar o devolver diseño con acciones concretas. |

## Ejercicios de Clase

### C16-E01 — Historias verticales
**Especificación:** Escribir 8 historias con criterios Given/When/Then y prioridad MoSCoW.
**Entregable:** Archivo `backlog.md` en la raíz del proyecto.
**Criterios de Aceptación:** Cada historia produce valor observable y tiene criterio verificable.
**Comando de verificación:** Revisión manual del archivo `backlog.md`.

### C16-E02 — Diagrama C4
**Especificación:** Crear contexto/contenedores con Spring, DB, Temporal, broker y proveedor IA.
**Entregable:** Archivo Mermaid/PlantUML (ej. `arquitectura.mmd`).
**Criterios de Aceptación:** Responsabilidades y protocolos explícitos; no “caja mágica”.
**Comando de verificación:** Revisión manual del diagrama.

### C16-E03 — Secuencia crítica
**Especificación:** Definir estados, Activities, mensajes, timeouts, retries y compensaciones.
**Entregable:** Archivo `workflow-design.md`.
**Criterios de Aceptación:** Determinismo y idempotencia revisados.
**Comando de verificación:** Revisión manual del diseño.

### C16-E04 — Contratos mínimos
**Especificación:** Diseñar tablas, endpoints, errores y eventos v1.
**Entregable:** OpenAPI (`openapi.yaml`) + migración inicial (`V1__init.sql`) + schema evento.
**Criterios de Aceptación:** IDs/correlación coherentes; datos sensibles clasificados.
**Comando de verificación:** `./mvnw clean compile` (para validar sintaxis si se usa generador OpenAPI) o revisión manual.

### C16-E05 — Threat model del proyecto
**Especificación:** DFD, trust boundaries y top 8 riesgos.
**Entregable:** Archivo `threat-model.md`.
**Criterios de Aceptación:** Mitigaciones asignadas a historias.
**Comando de verificación:** Revisión manual.

### C16-E06 — Plan de verificación
**Especificación:** Matriz requisito→tipo de prueba→fixture→evidencia.
**Entregable:** Archivo `test-plan.md`.
**Criterios de Aceptación:** Incluye replay, chaos, security y AI eval si aplica.
**Comando de verificación:** Revisión manual.

### C16-E07 — Issues y Definition of Done
**Especificación:** Crear issues de 30–90 min con dependencia y dueño.
**Entregable:** Issue board/export (ej. `issues.csv` o captura).
**Criterios de Aceptación:** Ningún issue “hacer backend completo”; DoD incluye pruebas/docs.
**Comando de verificación:** Revisión manual.

### C16-E08 — Camino mínimo (Walking Skeleton)
**Especificación:** POST inicia Workflow, una Activity guarda/consulta y GET muestra estado.
**Entregable:** Commit ejecutable con código base en `src/main/java/com/sigeo/clase16/`.
**Criterios de Aceptación:** Arranca con un comando; health de app/DB/Temporal visible.
**Comando de verificación:** `./mvnw spring-boot:run` y `curl http://localhost:8080/actuator/health`

## Tareas para el Hogar

### C16-T01 — Sprint 1
**Esfuerzo:** 60-90 min
**Especificación:** Implementar dominio, DB, API inicial y seguridad base.
**Entregable:** Release candidate 0.1.
**Criterios de Aceptación:** CI local verde; 30 pruebas; demo de 3 min.

### C16-T02 — Sprint 2
**Esfuerzo:** 60-90 min
**Especificación:** Implementar Workflow/Saga con Activities idempotentes y consultas.
**Entregable:** Release candidate 0.2.
**Criterios de Aceptación:** Fault injection en al menos 3 pasos.

### C16-T03 — Sprint 3
**Esfuerzo:** 60-90 min
**Especificación:** Integrar mensajería y, opcionalmente, IA con evaluación/fallback.
**Entregable:** Release candidate 0.3.
**Criterios de Aceptación:** DLQ/dedupe o AI guardrails demostrables.

### C16-T04 — Documentación de entrega
**Esfuerzo:** 60-90 min
**Especificación:** Completar README, diagramas, ADR, runbook, threat model y matriz de pruebas.
**Entregable:** docs release.
**Criterios de Aceptación:** Un tercero puede levantar y probar el sistema.

## Cómo ejecutar

Para levantar la infraestructura local (PostgreSQL, Temporal, RabbitMQ):
```bash
docker compose up -d
```
*(Nota: Si usas Temporal CLI, puedes ejecutar `temporal server start-dev` en su lugar)*

Para ejecutar las pruebas del Walking Skeleton:
```bash
./mvnw test
```

Para iniciar la aplicación:
```bash
./mvnw spring-boot:run
```
