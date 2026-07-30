# Clase 07: Transacciones, concurrencia, depuración web, pruebas y documentación viva

**Bloque:** Bloque 2 — Aplicaciones web y persistencia
**Duración:** 4 h

## Objetivos de aprendizaje
- Definir límites de transacción en servicios y explicar propagación/rollback.
- Resolver lost update con bloqueo optimista y conocer casos de bloqueo pesimista.
- Diseñar pruebas unitarias, de slice e integración sin sobreusar mocks.
- Depurar una petición completa desde HTTP hasta SQL en VS Code.
- Mantener OpenAPI, ADR, diagramas y runbooks como documentación viva.

## Cronograma de la clase

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Caso de carrera inicial | Simular dos aprobaciones concurrentes. |
| 10–35 | Transacciones y propagación | Dibujar límites y mostrar rollback. |
| 35–60 | Demo optimistic locking | Ejecutar dos requests y analizar excepción. |
| 60–80 | Ejercicios E01–E03 | Transacciones y conflicto. |
| 80–95 | Receso | Preparar suite de pruebas. |
| 95–120 | Estrategia de pruebas | Comparar test unitario, slice e integración. |
| 120–160 | Laboratorio E04–E06 | Debug y Testcontainers. |
| 160–185 | Desafíos E07–E08 | OpenAPI, ADR y arquitectura. |
| 185–195 | Cierre y tarea | Autoevaluación con Definition of Done. |

## Ejercicios de clase

### C07-E01 — Rollback total
**Especificación:** Servicio crea aprobación y actualiza solicitud; inducir fallo y verificar atomicidad.
**Criterios de aceptación:** Tras fallo, ninguna escritura parcial queda confirmada.
**Archivos involucrados:** `SolicitudService.java`, `SolicitudServiceIntegrationTest.java`
**Comando para verificar:** `./mvnw test -Dtest=SolicitudServiceIntegrationTest#testRollbackTotal`

### C07-E02 — Self-invocation
**Especificación:** Reproducir método @Transactional llamado internamente que no obtiene semántica esperada.
**Criterios de aceptación:** Identifica proxy como causa; solución no depende de “magia”.
**Archivos involucrados:** `SelfInvocationService.java`, `SelfInvocationTest.java`
**Comando para verificar:** `./mvnw test -Dtest=SelfInvocationTest`

### C07-E03 — Conflicto optimista
**Especificación:** Dos actualizaciones con la misma versión; traducir conflicto a 409.
**Criterios de aceptación:** Una gana, otra recibe conflicto; no se pierden datos silenciosamente.
**Archivos involucrados:** `Solicitud.java`, `SolicitudController.java`, `OptimisticLockingTest.java`
**Comando para verificar:** `./mvnw test -Dtest=OptimisticLockingTest`

### C07-E04 — Servicio de aprobación
**Especificación:** Probar reglas con fake/mock mínimo y test data builder.
**Criterios de aceptación:** No inicia Spring; casos de borde claros.
**Archivos involucrados:** `AprobacionService.java`, `AprobacionServiceTest.java`
**Comando para verificar:** `./mvnw test -Dtest=AprobacionServiceTest`

### C07-E05 — Controller aislado
**Especificación:** @WebMvcTest para contrato, validación y Problem Details.
**Criterios de aceptación:** Servicio simulado; cuerpo y headers verificados.
**Archivos involucrados:** `SolicitudController.java`, `SolicitudControllerSliceTest.java`
**Comando para verificar:** `./mvnw test -Dtest=SolicitudControllerSliceTest`

### C07-E06 — Flujo HTTP→DB
**Especificación:** @SpringBootTest + Testcontainers para crear, consultar y actualizar.
**Criterios de aceptación:** DB real; datos aislados; no depende de orden.
**Archivos involucrados:** `SolicitudIntegrationTest.java`
**Comando para verificar:** `./mvnw test -Dtest=SolicitudIntegrationTest`

### C07-E07 — Petición lenta
**Especificación:** Seguir request con correlationId y hallar consulta inesperada.
**Criterios de aceptación:** Causa demostrada con stack/SQL; corrección medida.
**Archivos involucrados:** `debug-trace.md`
**Comando para verificar:** Revisión manual del archivo `debug-trace.md`

### C07-E08 — OpenAPI + ADR
**Especificación:** Documentar endpoint de transición y decisión de optimistic locking.
**Criterios de aceptación:** Ejemplos 200/409; consecuencias y alternativa evaluada.
**Archivos involucrados:** `openapi.yaml`, `001-optimistic-locking.md`
**Comando para verificar:** Revisión manual de los archivos generados.

## Tareas para el hogar

### C07-T01 — Suite por capas
**Esfuerzo:** 60-90 min
**Especificación:** Construir 12 unit tests, 8 slice tests y 5 integration tests para SIGEO.
**Criterios de aceptación:** No duplicar el mismo caso en todas las capas sin propósito.

### C07-T02 — Simulador de concurrencia
**Esfuerzo:** 60-90 min
**Especificación:** Script que lance 20 actualizaciones concurrentes y reporte éxitos/conflictos.
**Criterios de aceptación:** No hay lost updates; resultados repetibles.

### C07-T03 — Runbook de fallos
**Esfuerzo:** 60-90 min
**Especificación:** Documentar diagnóstico de app caída, DB no disponible, migración fallida y petición lenta.
**Criterios de aceptación:** Comandos concretos, señales esperadas y escalamiento.

### C07-T04 — Documentación viva
**Esfuerzo:** 60-90 min
**Especificación:** Actualizar diagrama C4/mermaid, OpenAPI y ADR con cambios reales.
**Criterios de aceptación:** Cada artefacto enlaza al código relevante.

## Cómo ejecutar
1. Compilar y ejecutar pruebas:
   ```bash
   cd ejercicios
   ./mvnw clean test
   ```
2. Ejecutar la aplicación (requiere base de datos PostgreSQL):
   ```bash
   cd ejercicios
   docker compose up -d
   ./mvnw spring-boot:run
   ```
