# Clase 10: Activities, timeouts, reintentos, heartbeats e idempotencia

**Bloque:** Bloque 3 — Workflows resilientes y sistemas distribuidos
**Duración:** 4 horas

## Objetivos de Aprendizaje
- Configurar Start-to-Close, Schedule-to-Close, Schedule-to-Start y Heartbeat timeouts según el caso.
- Diseñar RetryOptions y clasificar errores no reintentables.
- Hacer Activities idempotentes usando claves de negocio y registros de deduplicación.
- Emitir heartbeats y reanudar progreso de actividades largas.
- Aplicar cancelación y compensación sin reintentos infinitos.

## Cronograma de la Clase

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Quiz de timeouts | Elegir timeout para 5 Activities. |
| 10–35 | Semántica y reintentos | Dibujar intentos y backoff. |
| 35–60 | Demo Activity inestable | Observar intentos en UI. |
| 60–80 | Ejercicios E01–E03 | Timeouts, retry y error classification. |
| 80–95 | Receso | Preparar Activity larga. |
| 95–120 | Idempotencia y heartbeats | Mostrar deduplicación y resume. |
| 120–160 | Laboratorio E04–E06 | Procesamiento de lote y cancelación. |
| 160–185 | Desafíos E07–E08 | Métricas y fault matrix. |
| 185–195 | Cierre y tarea | Revisión de política de fallos. |

## Ejercicios de Clase

### C10-E01 — Actividad HTTP acotada
**Especificación:** Configurar timeouts para llamada externa de 2 s y simular latencias 1/3/10 s.
**Entregable:** ActivityOptions y tabla de resultados.
**Criterios de Aceptación:** Falla dentro de tiempo previsto; no depende de timeout infinito.
**Archivos involucrados:** `HttpActivity.java`, `HttpWorkflow.java`, `HttpWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=HttpWorkflowTest`

### C10-E02 — Servicio 503 temporal
**Especificación:** Reintentar 503 con backoff y detener ante 400.
**Entregable:** RetryOptions y tests.
**Criterios de Aceptación:** 400 clasificado no reintentable; máximo de intentos explícito.
**Archivos involucrados:** `ServiceActivity.java`, `ServiceWorkflow.java`, `ServiceWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=ServiceWorkflowTest`

### C10-E03 — ApplicationFailure tipada
**Especificación:** Emitir códigos VALIDATION, NOT_FOUND y PROVIDER_UNAVAILABLE.
**Entregable:** Activity y manejo en Workflow.
**Criterios de Aceptación:** Workflow decide según tipo, no parsea mensajes.
**Archivos involucrados:** `TypedFailureActivity.java`, `TypedFailureWorkflow.java`, `TypedFailureWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=TypedFailureWorkflowTest`

### C10-E04 — Reserva única
**Especificación:** Activity de reserva acepta idempotency key y evita duplicados al repetirse.
**Entregable:** Repositorio fake y test de doble invocación.
**Criterios de Aceptación:** Mismo comando retorna mismo resultado sin segunda reserva.
**Archivos involucrados:** `ReservationActivity.java`, `ReservationWorkflow.java`, `ReservationWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=ReservationWorkflowTest`

### C10-E05 — Procesamiento por páginas
**Especificación:** Procesar 1000 registros por páginas, heartbeat de último offset y reanudar.
**Entregable:** Activity y prueba de interrupción.
**Criterios de Aceptación:** No reprocesa más de la ventana permitida; progreso visible.
**Archivos involucrados:** `BatchProcessingActivity.java`, `BatchProcessingWorkflow.java`, `BatchProcessingWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=BatchProcessingWorkflowTest`

### C10-E06 — Cancelar exportación
**Especificación:** Detectar cancelación durante Activity larga y cerrar recursos.
**Entregable:** Workflow/Activity y test.
**Criterios de Aceptación:** Cancelación cooperativa; cleanup idempotente.
**Archivos involucrados:** `ExportActivity.java`, `ExportWorkflow.java`, `ExportWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=ExportWorkflowTest`

### C10-E07 — Intento y latencia
**Especificación:** Agregar logs/metrics con workflowId, activityId e intento sin duplicar datos sensibles.
**Entregable:** Salida y panel textual.
**Criterios de Aceptación:** Permite distinguir intento y causa; no imprime payload completo.
**Archivos involucrados:** `LoggingActivity.java`, `LoggingWorkflow.java`, `LoggingWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=LoggingWorkflowTest`

### C10-E08 — Tabla de resiliencia
**Especificación:** Ejecutar 8 combinaciones de fallo/timeout/retry y documentar resultado esperado/real.
**Entregable:** `resilience-matrix.md`.
**Criterios de Aceptación:** Coincidencia razonada; anomalías investigadas.
**Archivos involucrados:** `resilience-matrix.md`
**Comando para verificar:** Revisión manual.

## Tareas para el Hogar

### C10-T01 — Integración inestable
**Esfuerzo:** 60-90 min
**Especificación:** Workflow llama a catálogo y notificación simulados con fallos programables, timeouts y retry por tipo.
**Entregable:** Módulo y 15 pruebas.
**Criterios de Aceptación:** Sin retry infinito; errores permanentes terminan rápido.

### C10-T02 — Actividad idempotente real
**Esfuerzo:** 60-90 min
**Especificación:** Persistir deduplicación en PostgreSQL con clave única y manejar concurrencia.
**Entregable:** Activity y migración.
**Criterios de Aceptación:** Dos ejecuciones concurrentes producen un solo efecto.

### C10-T03 — Activity larga reanudable
**Esfuerzo:** 60-90 min
**Especificación:** Importar archivo grande con heartbeat de offset y cancelación.
**Entregable:** Implementación y prueba de reinicio.
**Criterios de Aceptación:** Reanuda desde progreso; cierre seguro de archivo.

### C10-T04 — Runbook de Activity fallida
**Esfuerzo:** 60-90 min
**Especificación:** Procedimiento para inspeccionar, resetear/reintentar o corregir un fallo sin manipular DB a ciegas.
**Entregable:** `docs/activity-runbook.md`.
**Criterios de Aceptación:** Incluye criterios para fallo transitorio/permanente.

## Cómo ejecutar

Para ejecutar los tests de la clase:
```bash
./mvnw clean test
```

Para ejecutar un test específico:
```bash
./mvnw test -Dtest=NombreDelTest
```

Para iniciar el servidor de Temporal en modo desarrollo (si se requiere probar manualmente):
```bash
temporal server start-dev
```
