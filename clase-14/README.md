# Clase 14: Inteligencia artificial en aplicaciones Java y agentes durables

**Bloque:** Bloque 4 — IA y tecnologías avanzadas  
**Duración:** 4 horas

## Objetivos de aprendizaje
- Usar Spring AI para chat, structured output y tool calling con configuración externalizada.
- Diseñar prompts con contrato, contexto, límites y validación de salida.
- Implementar RAG básico con chunking, embeddings y vector store.
- Ejecutar llamadas de modelo y herramientas externas como Activities Temporal.
- Aplicar guardrails, evaluación, privacidad, costos y fallback.

## Cronograma de la clase

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Evaluación de casos de uso | Clasificar IA necesaria, útil o innecesaria. |
| 10–35 | Fundamentos y Spring AI | Explicar variabilidad y contrato. |
| 35–60 | Demo structured output | Validar JSON/DTO y fallback. |
| 60–80 | Ejercicios E01–E03 | Prompts, DTO y tool calling. |
| 80–95 | Receso | Preparar documentos RAG. |
| 95–120 | RAG y seguridad | Mostrar retrieval y prompt injection. |
| 120–160 | Laboratorio E04–E06 | RAG + Activity Temporal. |
| 160–185 | Desafíos E07–E08 | Evaluación y costos. |
| 185–195 | Cierre y preparación de visita | Entregar guía de observación para visita profesional del 26–30 OCT. |

## Ejercicios de clase

### C14-E01 — Clasificador estructurado
**Especificación:** Diseñar prompt que clasifique solicitud y devuelva DTO con categoría, urgencia y explicación breve.
**Criterios de aceptación:** Salida validada; valores fuera de enum rechazados.
**Archivos involucrados:** `ClasificadorService.java`, `ClasificacionDTO.java`, `ClasificadorServiceTest.java`
**Comando para verificar:** `./mvnw test -Dtest=ClasificadorServiceTest`

### C14-E02 — Fallback sin IA
**Especificación:** Ante timeout o salida inválida, usar clasificación determinista simple.
**Criterios de aceptación:** La operación crítica continúa; fallo de IA es observable.
**Archivos involucrados:** `ClasificadorService.java`, `ClasificadorServiceTest.java`
**Comando para verificar:** `./mvnw test -Dtest=ClasificadorServiceTest`

### C14-E03 — Consulta de catálogo
**Especificación:** Exponer herramienta read-only para consultar recursos; el modelo no modifica DB.
**Criterios de aceptación:** Allowlist; valida parámetros; autorización en backend.
**Archivos involucrados:** `CatalogoTools.java`, `AsistenteService.java`, `AsistenteServiceTest.java`
**Comando para verificar:** `./mvnw test -Dtest=AsistenteServiceTest`

### C14-E04 — Asistente de normativa
**Especificación:** Ingerir 3 documentos, recuperar fragmentos y responder con referencias internas.
**Criterios de aceptación:** Respuesta distingue "no encontrado"; muestra fuente/chunk.
**Archivos involucrados:** `NormativaRagService.java`, `NormativaRagServiceTest.java`
**Comando para verificar:** `./mvnw test -Dtest=NormativaRagServiceTest`

### C14-E05 — Prompt injection lab
**Especificación:** Probar documentos que intentan cambiar instrucciones y mitigar mediante separación de roles/allowlist.
**Criterios de aceptación:** No ejecuta herramienta no autorizada ni revela prompt/secretos.
**Archivos involucrados:** `SeguridadAiService.java`, `SeguridadAiServiceTest.java`
**Comando para verificar:** `./mvnw test -Dtest=SeguridadAiServiceTest`

### C14-E06 — Análisis durable
**Especificación:** Llamar al modelo desde Activity con timeout, retry limitado y registro de modelo/promptVersion.
**Criterios de aceptación:** No model call en Workflow; error permanente no se reintenta sin límite.
**Archivos involucrados:** `AnalisisAiActivity.java`, `AnalisisAiActivityImpl.java`, `AnalisisWorkflow.java`, `AnalisisWorkflowImpl.java`, `AnalisisWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=AnalisisWorkflowTest`

### C14-E07 — Conjunto dorado
**Especificación:** Crear 20 preguntas/respuestas esperadas y medir exactitud, abstención y fuentes.
**Criterios de aceptación:** Métricas definidas; casos fallidos analizados.
**Archivos involucrados:** `EvaluacionAiTest.java`
**Comando para verificar:** `./mvnw test -Dtest=EvaluacionAiTest`

### C14-E08 — Presupuesto de tokens
**Especificación:** Comparar dos configuraciones y establecer límites de tokens/latencia/costo simulado.
**Criterios de aceptación:** Decisión basada en datos y calidad mínima.
**Archivos involucrados:** `application.yaml`, `PresupuestoTest.java`
**Comando para verificar:** `./mvnw test -Dtest=PresupuestoTest`

## Tareas para el hogar

### C14-T01 — Asistente SIGEO
**Especificación:** Implementar ayuda contextual con structured output y RAG sobre documentación del sistema.
**Criterios de aceptación:** No toma decisiones irreversibles; fuentes visibles.

### C14-T02 — AI Activity resiliente
**Especificación:** Integrar llamada de IA como Activity con retry, timeout, fallback y trazabilidad de versión.
**Criterios de aceptación:** Determinismo preservado; payloads minimizados.

### C14-T03 — Red-team de prompts
**Especificación:** Crear 15 ataques de inyección/exfiltración/tool abuse y registrar mitigaciones.
**Criterios de aceptación:** Severidad, evidencia y prueba regresiva.

### C14-T04 — Guía visita profesional
**Especificación:** Preparar 12 preguntas sobre arquitectura, seguridad, DevOps, mensajería, Temporal/alternativas e IA responsable.
**Criterios de aceptación:** Preguntas abiertas y vinculadas al curso.

## Cómo ejecutar

1. Iniciar servidor Temporal en desarrollo:
   ```bash
   temporal server start-dev
   ```
2. Iniciar RabbitMQ (si aplica):
   ```bash
   docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
   ```
3. Configurar variable de entorno para Spring AI (usando un mock o clave real para pruebas locales):
   ```bash
   export OPENAI_API_KEY="tu-api-key"
   ```
4. Ejecutar tests:
   ```bash
   ./mvnw test
   ```
