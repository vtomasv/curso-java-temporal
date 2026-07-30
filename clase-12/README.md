# Clase 12: Microservicios, transacciones distribuidas y patrón Saga

**Bloque:** Bloque 3 — Workflows resilientes y sistemas distribuidos
**Duración:** 4 horas

## Objetivos de aprendizaje
- Explicar por qué una transacción ACID no cruza microservicios de forma práctica.
- Distinguir saga orquestada y coreografiada, y seleccionar Temporal para orquestación.
- Diseñar pasos, compensaciones y orden inverso de rollback.
- Manejar fallos de compensación y estados parcialmente compensados.
- Integrar Spring Boot, PostgreSQL y Temporal sin mezclar transacciones locales con Workflow state.

## Cronograma de la clase

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Juego de estados parciales | Analizar qué revertir en cinco fallos. |
| 10–35 | Transacciones distribuidas | Comparar 2PC, saga y coreografía. |
| 35–60 | Demo Saga Temporal | Ejecutar flujo con fallo inducido. |
| 60–80 | Ejercicios E01–E03 | Pasos y compensaciones. |
| 80–95 | Receso | Preparar servicios stub. |
| 95–120 | Fallos de compensación e idempotencia | Mostrar estado “requiere intervención”. |
| 120–160 | Laboratorio E04–E06 | Saga completa. |
| 160–185 | Desafíos E07–E08 | Outbox y child workflow. |
| 185–195 | Cierre y tarea | Defensa de diseño de compensación. |

## Ejercicios de clase

### C12-E01 — Mapa de pasos
**Especificación:** Definir pasos reserva-presupuesto-agenda-notificación y compensación de cada uno.
**Criterios de aceptación:** Compensaciones son semánticas, no “rollback SQL remoto”.
**Archivos involucrados:** `SagaSteps.java`
**Comando para verificar:** `./mvnw test -Dtest=SagaStepsTest`

### C12-E02 — Saga mínima
**Especificación:** Implementar dos pasos y compensar el primero si falla el segundo.
**Criterios de aceptación:** Compensación registrada en orden seguro e idempotente.
**Archivos involucrados:** `SagaWorkflowImpl.java`, `SagaActivities.java`
**Comando para verificar:** `./mvnw test -Dtest=SagaWorkflowTest#testSagaMinima`

### C12-E03 — Fallo por etapa
**Especificación:** Parametrizar fallo en cada paso y verificar estado final.
**Criterios de aceptación:** Todos los recursos quedan liberados o marcados para intervención.
**Archivos involucrados:** `SagaWorkflowImpl.java`, `SagaWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=SagaWorkflowTest#testFalloPorEtapa`

### C12-E04 — Compensación inestable
**Especificación:** Hacer fallar liberación temporalmente y configurar retry diferente.
**Criterios de aceptación:** No pierde necesidad de compensar; visibilidad del fallo.
**Archivos involucrados:** `SagaWorkflowImpl.java`, `SagaActivities.java`
**Comando para verificar:** `./mvnw test -Dtest=SagaWorkflowTest#testCompensacionInestable`

### C12-E05 — Doble cancelación
**Especificación:** Compensar dos veces sin error ni efecto duplicado.
**Criterios de aceptación:** Resultado estable ante repetición y concurrencia.
**Archivos involucrados:** `SagaActivitiesImpl.java`, `SagaActivitiesTest.java`
**Comando para verificar:** `./mvnw test -Dtest=SagaActivitiesTest#testDobleCancelacion`

### C12-E06 — Endpoint de operación
**Especificación:** POST inicia saga y GET consulta estado/resultado.
**Criterios de aceptación:** HTTP no espera indefinidamente; IDs correlacionados.
**Archivos involucrados:** `SagaController.java`
**Comando para verificar:** `./mvnw test -Dtest=SagaControllerTest`

### C12-E07 — Contextos separados
**Especificación:** Convertir reserva de recurso y presupuesto en child workflows.
**Criterios de aceptación:** Task queues y ownership definidos.
**Archivos involucrados:** `SagaWorkflowImpl.java`, `ResourceChildWorkflow.java`
**Comando para verificar:** `./mvnw test -Dtest=SagaWorkflowTest#testChildWorkflows`

### C12-E08 — Evento de completitud
**Especificación:** Diseñar tabla outbox y publicador idempotente al finalizar saga.
**Criterios de aceptación:** Evita commit DB + publish no atómico; clave de dedupe definida.
**Archivos involucrados:** `OutboxService.java`, `OutboxTest.java`
**Comando para verificar:** `./mvnw test -Dtest=OutboxTest`

## Tareas para el hogar

### C12-T01 — Saga de asignación
**Esfuerzo:** 60-90 min
**Especificación:** Implementar saga de 4 pasos con 4 fallos inducibles y compensaciones.
**Criterios de aceptación:** Estados finales documentados; no hay efectos duplicados.

### C12-T02 — Panel de seguimiento
**Esfuerzo:** 60-90 min
**Especificación:** Exponer estado de saga, pasos completados y compensaciones desde Query/DB.
**Criterios de aceptación:** No usa Query para I/O; combina fuentes en capa de consulta.

### C12-T03 — Chaos script
**Esfuerzo:** 60-90 min
**Especificación:** Ejecutar 50 sagas con probabilidades de fallo y resumir resultados.
**Criterios de aceptación:** Cero invariantes rotas; fallos pendientes identificables.

### C12-T04 — ADR de consistencia
**Esfuerzo:** 60-90 min
**Especificación:** Comparar saga orquestada, coreografía y 2PC para el caso.
**Criterios de aceptación:** Incluye operación, observabilidad y recuperación.

## Cómo ejecutar
1. Iniciar servidor Temporal localmente:
   ```bash
   temporal server start-dev
   ```
2. Ejecutar los tests:
   ```bash
   ./mvnw test
   ```
3. Ejecutar la aplicación Spring Boot:
   ```bash
   ./mvnw spring-boot:run
   ```
