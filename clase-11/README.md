# Clase 11: Interacción con Workflows: Signals, Queries, Updates, timers y Continue-As-New

**Bloque:** Bloque 3 — Workflows resilientes y sistemas distribuidos  
**Duración:** 4 horas

## Objetivos de Aprendizaje
- Usar Signals para eventos asíncronos, Queries para lectura y Updates para operaciones confirmadas.
- Validar mensajes y evitar condiciones de carrera lógicas en el Workflow.
- Esperar condiciones con `Workflow.await` y usar timers durables.
- Orquestar Child Workflows y manejar su política de cierre.
- Aplicar Continue-As-New para limitar Event History y preservar estado esencial.

## Cronograma de la Clase

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Clasificación de comandos | Elegir Signal/Query/Update para 8 casos. |
| 10–35 | Message passing | Explicar garantías y restricciones. |
| 35–60 | Demo aprobación con Signal/Query | Interactuar desde CLI/cliente. |
| 60–80 | Ejercicios E01–E03 | Signals, queries y await. |
| 80–95 | Receso | Preparar Updates y child workflow. |
| 95–120 | Updates, hijos y CAN | Mostrar validación y continuidad. |
| 120–160 | Laboratorio E04–E06 | Proceso interactivo completo. |
| 160–185 | Desafíos E07–E08 | Deduplicación e history. |
| 185–195 | Cierre y tarea | Ticket: justificar Signal vs Update. |

## Ejercicios de Clase

### C11-E01 — Aprobar o rechazar
**Especificación:** Agregar señales `approve` y `reject` a un Workflow en espera.
**Criterios de aceptación:** Ignora transición inválida de forma definida; resultado durable.
**Archivos involucrados:** `ApprovalWorkflow.java`, `ApprovalWorkflowImpl.java`, `ApprovalWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=ApprovalWorkflowTest#testApproveSignal`

### C11-E02 — Estado consultable
**Especificación:** Exponer estado, historial resumido y deadline sin modificar Workflow.
**Criterios de aceptación:** Query no hace I/O ni muta estado.
**Archivos involucrados:** `ApprovalWorkflow.java`, `ApprovalWorkflowImpl.java`, `ApprovalWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=ApprovalWorkflowTest#testQueryState`

### C11-E03 — Vencimiento automático
**Especificación:** Esperar decisión o timeout, lo que ocurra primero.
**Criterios de aceptación:** No usar `Thread.sleep`; resultado correcto en ambos caminos.
**Archivos involucrados:** `ApprovalWorkflow.java`, `ApprovalWorkflowImpl.java`, `ApprovalWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=ApprovalWorkflowTest#testTimeout`

### C11-E04 — Cambiar prioridad confirmado
**Especificación:** Update valida estado y retorna nueva prioridad.
**Criterios de aceptación:** Entrada inválida rechazada antes de handler; respuesta confirmada.
**Archivos involucrados:** `ApprovalWorkflow.java`, `ApprovalWorkflowImpl.java`, `ApprovalWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=ApprovalWorkflowTest#testUpdatePriority`

### C11-E05 — Comando repetido
**Especificación:** Incluir `commandId` y evitar procesar dos veces misma aprobación.
**Criterios de aceptación:** Reintento del cliente no duplica transición.
**Archivos involucrados:** `ApprovalWorkflow.java`, `ApprovalWorkflowImpl.java`, `ApprovalWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=ApprovalWorkflowTest#testDeduplication`

### C11-E06 — Revisión especializada
**Especificación:** Delegar evaluación técnica a child workflow con timeout/cancelación.
**Criterios de aceptación:** Parent close policy explícita; errores propagados o manejados.
**Archivos involucrados:** `ApprovalWorkflowImpl.java`, `TechnicalReviewWorkflow.java`, `TechnicalReviewWorkflowImpl.java`, `ApprovalWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=ApprovalWorkflowTest#testChildWorkflow`

### C11-E07 — Bandeja de eventos larga
**Especificación:** Tras N eventos, continuar como nuevo conservando estado compacto.
**Criterios de aceptación:** History se reinicia; estado esencial preservado.
**Archivos involucrados:** `LongRunningWorkflow.java`, `LongRunningWorkflowImpl.java`, `LongRunningWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=LongRunningWorkflowTest`

### C11-E08 — Aprobación vs expiración
**Especificación:** Simular señal cercana al timer y definir política determinista.
**Criterios de aceptación:** Resultado consistente con regla explícita.
**Archivos involucrados:** `ApprovalWorkflowImpl.java`, `ApprovalWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=ApprovalWorkflowTest#testRaceCondition`

## Tareas para el Hogar

### C11-T01 — Aprobación multinivel
**Especificación:** Workflow con dos niveles, Signals/Updates, Queries y vencimientos por etapa.
**Entregable:** Módulo y 20 pruebas.
**Criterios:** Interacciones idempotentes; estados exhaustivos.

### C11-T02 — Cliente operativo
**Especificación:** CLI o endpoints Spring para iniciar, consultar, actualizar, señalar y cancelar Workflows.
**Entregable:** Cliente y colección HTTP.
**Criterios:** Errores de workflow no se traducen a 500 genérico.

### C11-T03 — Continue-As-New controlado
**Especificación:** Procesar 500 eventos simulados y continuar cada 50; registrar runs.
**Entregable:** Informe y tests.
**Criterios:** No acumula estado innecesario; búsquedas siguen siendo posibles.

### C11-T04 — Contrato de mensajes
**Especificación:** Definir versionado, commandId, validación y compatibilidad de Signals/Updates.
**Entregable:** `docs/workflow-messages.md`.
**Criterios:** Incluye estrategia para clientes antiguos.

## Cómo ejecutar

Para ejecutar los tests de los ejercicios:
```bash
cd ejercicios
./mvnw clean test
```

Para ejecutar el servidor de Temporal localmente (si deseas probar con un cliente real):
```bash
temporal server start-dev
```
