# Clase 09: Temporal.io: arquitectura y ejecución duradera

**Bloque:** Bloque 3 — Workflows resilientes y sistemas distribuidos  
**Duración:** 4 horas  

## Objetivos de aprendizaje
- Explicar por qué cron, colas y estados manuales no bastan para procesos largos.
- Identificar Temporal Service, Namespace, Task Queue, Worker, Workflow, Activity, Client y Event History.
- Crear y ejecutar un Workflow con interfaz e implementación Java.
- Observar replay, reinicio del Worker y continuidad del proceso.
- Aplicar reglas de determinismo desde el primer ejercicio.

## Cronograma de la clase

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Historia de un proceso fallido | Analizar un proceso de aprobación interrumpido. |
| 10–35 | Arquitectura Temporal | Dibujar componentes y responsabilidades. |
| 35–60 | Determinismo y Event History | Simular replay en pizarra. |
| 60–80 | Ejercicios E01–E03 | CLI, Workflow y Activity mínimos. |
| 80–95 | Receso | Verificar servidor local y UI. |
| 95–120 | Demo de durabilidad | Detener Worker durante Workflow.sleep y reanudar. |
| 120–160 | Laboratorio E04–E06 | Aprobación durable básica. |
| 160–185 | Desafíos E07–E08 | Determinism review e historia. |
| 185–195 | Cierre y tarea | Ticket: clasificar 8 operaciones como Workflow o Activity. |

## Cómo ejecutar

Para iniciar el servidor de desarrollo de Temporal:
```bash
temporal server start-dev
```

Para ejecutar los tests de los ejercicios:
```bash
./mvnw test
```

## Ejercicios de clase

### C09-E01 — Temporal local
**Especificación:** Iniciar Temporal CLI dev server, abrir UI y describir namespace/task queue.
**Criterios de aceptación:** Servidor accesible; no usar Docker Compose legado si CLI está disponible.
**Archivos involucrados:** N/A (Solo comandos)
**Comando para verificar:** `temporal server start-dev` y acceder a http://localhost:8233

### C09-E02 — Saludo duradero
**Especificación:** Implementar interfaz `@WorkflowInterface` y método `@WorkflowMethod`.
**Criterios de aceptación:** Workflow ID explícito; resultado visible en UI.
**Archivos involucrados:** `SaludoWorkflow.java`, `SaludoWorkflowImpl.java`, `SaludoWorker.java`, `SaludoWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=SaludoWorkflowTest`

### C09-E03 — Registrar auditoría
**Especificación:** Mover escritura simulada de auditoría a `@ActivityInterface`.
**Criterios de aceptación:** No hace I/O desde Workflow; Activity registrada en Worker.
**Archivos involucrados:** `AuditoriaActivity.java`, `AuditoriaActivityImpl.java`, `SaludoWorkflowImpl.java`, `SaludoWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=SaludoWorkflowTest`

### C09-E04 — Espera de revisión
**Especificación:** Usar `Workflow.sleep` para simular plazo y observar Timer events.
**Criterios de aceptación:** No `Thread.sleep`; replay correcto.
**Archivos involucrados:** `RevisionWorkflow.java`, `RevisionWorkflowImpl.java`, `RevisionWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=RevisionWorkflowTest`

### C09-E05 — Reinicio controlado
**Especificación:** Iniciar Workflow, detener Worker, esperar y reiniciar.
**Criterios de aceptación:** Mismo Workflow continúa; diferencia Workflow ID/Run ID explicada.
**Archivos involucrados:** `ReinicioWorker.java`
**Comando para verificar:** Ejecutar `ReinicioWorker` manualmente, detenerlo, y volver a ejecutarlo.

### C09-E06 — Aprobación v0
**Especificación:** Workflow que registra solicitud, espera plazo y marca vencida si no hay decisión simulada.
**Criterios de aceptación:** Estado solo en Workflow; Activity para persistencia/notificación.
**Archivos involucrados:** `AprobacionWorkflow.java`, `AprobacionWorkflowImpl.java`, `AprobacionActivity.java`, `AprobacionWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=AprobacionWorkflowTest`

### C09-E07 — Detectar no determinismo
**Especificación:** Encontrar 10 usos prohibidos en un Workflow: UUID, Instant.now, HTTP, DB, Thread, etc.
**Criterios de aceptación:** Cada corrección usa API Temporal o Activity adecuada.
**Archivos involucrados:** `NoDeterministaWorkflowImpl.java`
**Comando para verificar:** Revisión manual y corrección del código.

### C09-E08 — Leer la historia
**Especificación:** Etiquetar eventos de un run y relacionarlos con líneas del código.
**Criterios de aceptación:** Distingue Workflow Task, Activity Task y Timer.
**Archivos involucrados:** `history-walkthrough.md`
**Comando para verificar:** Revisión manual del documento.

## Tareas para el hogar

### C09-T01 — Workflow de expediente
**Especificación:** Orquestar creación, validación y notificación simulada con 3 Activities.
**Criterios de aceptación:** Determinismo revisado; Workflow ID de negocio; 8 pruebas.

### C09-T02 — Guía de reglas Temporal
**Especificación:** Crear checklist de código permitido/prohibido dentro de Workflow con ejemplos Java.
**Criterios de aceptación:** Incluye reloj, aleatoriedad, I/O, threads, config y versionado.

### C09-T03 — Análisis de historia
**Especificación:** Exportar una Event History y explicar 15 eventos relevantes.
**Criterios de aceptación:** Relaciona comandos con eventos y reintentos.

### C09-T04 — Integración Spring inicial
**Especificación:** Crear aplicación Spring Boot que inyecte WorkflowClient e inicie Workflow desde endpoint.
**Criterios de aceptación:** Controller no contiene lógica de orquestación; configuración externalizada.
