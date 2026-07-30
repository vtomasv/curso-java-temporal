# C09-E08 — Leer la historia

## Instrucciones
Exporta el Event History de un workflow ejecutado en Temporal UI (formato JSON) o visualízalo en la interfaz.
Luego, completa la siguiente tabla relacionando los eventos clave con las líneas de código de tu Workflow.

| ID Evento | Tipo de Evento | Descripción / Línea de código relacionada |
|-----------|----------------|-------------------------------------------|
| 1         | WorkflowExecutionStarted | TODO(C09-E08): Describir qué inicia esto |
| 2         | WorkflowTaskScheduled    | TODO(C09-E08): Describir |
| ...       | ...            | ... |
| X         | TimerStarted   | TODO(C09-E08): Relacionar con Workflow.sleep |
| Y         | ActivityTaskScheduled | TODO(C09-E08): Relacionar con llamada a Activity |
| Z         | WorkflowExecutionCompleted | TODO(C09-E08): Relacionar con el return del método |

## Preguntas
1. ¿Qué diferencia hay entre `WorkflowTaskScheduled` y `ActivityTaskScheduled`?
   - Respuesta: TODO(C09-E08)

2. Cuando el Worker se reinicia y hace replay, ¿qué eventos se vuelven a ejecutar y cuáles no?
   - Respuesta: TODO(C09-E08)
