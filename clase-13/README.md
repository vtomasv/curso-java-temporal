# Clase 13: Pruebas Temporal, replay, versionado, observabilidad y seguridad

**Bloque:** Bloque 3 — Workflows resilientes y sistemas distribuidos  
**Duración:** 4 horas  

## Objetivos de Aprendizaje
- Escribir pruebas de integración Temporal con `TestWorkflowEnvironment` y salto de tiempo.
- Aplicar replay testing contra historias reales antes de desplegar cambios.
- Usar versionado/patching y Continue-As-New para cambios compatibles.
- Instrumentar métricas, tracing, logs y Search Attributes sin filtrar datos sensibles.
- Configurar acceso local/cloud con API key o mTLS y separar secretos/payloads.

## Cronograma de la Clase

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Revisión de suite actual | Clasificar tests lentos/frágiles. |
| 10–35 | Testing y time skipping | Construir un test de vencimiento. |
| 35–60 | Replay/versioning demo | Cambiar código y observar nondeterminism. |
| 60–80 | Ejercicios E01–E03 | Test env, replay y versionado. |
| 80–95 | Receso | Preparar observabilidad. |
| 95–120 | Metrics, visibility y seguridad | Distinguir Search Attribute/Memo/payload. |
| 120–160 | Laboratorio E04–E06 | Operabilidad y conexión segura. |
| 160–185 | Desafíos E07–E08 | Gate CI y auditoría de datos. |
| 185–195 | Cierre y tarea | Checklist de despliegue firmado por equipo. |

## Ejercicios de Clase

### C13-E01 — Vencimiento en segundos
**Especificación:** Probar Workflow con timer de 30 días usando time skipping.
**Criterios de Aceptación:** Test dura segundos; código de producción sin reloj inyectado artificial.
**Archivos involucrados:** `VencimientoWorkflow.java`, `VencimientoWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=VencimientoWorkflowTest`

### C13-E02 — Signals y Updates
**Especificación:** Probar señal, query, update inválido y cancelación.
**Criterios de Aceptación:** Orden de interacción controlado; resultados exactos.
**Archivos involucrados:** `InteraccionWorkflow.java`, `InteraccionWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=InteraccionWorkflowTest`

### C13-E03 — Historia incompatible
**Especificación:** Ejecutar replay de historia y detectar cambio de orden de Activities.
**Criterios de Aceptación:** Falla antes del fix; pasa tras estrategia compatible.
**Archivos involucrados:** `ReplayWorkflowTest.java`, `historia_incompatible.json`
**Comando para verificar:** `./mvnw test -Dtest=ReplayWorkflowTest`

### C13-E04 — Nueva validación
**Especificación:** Introducir rama versionada para nuevos Workflows preservando los antiguos.
**Criterios de Aceptación:** Ambas versiones replayan; plan para retirar código antiguo.
**Archivos involucrados:** `VersionadoWorkflow.java`, `VersionadoWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=VersionadoWorkflowTest`

### C13-E05 — Search Attributes operativos
**Especificación:** Indexar estado, prioridad y responsable; consultar workflows.
**Criterios de Aceptación:** No coloca PII sensible; tipos de atributo correctos.
**Archivos involucrados:** `SearchAttributesWorkflow.java`, `SearchAttributesWorkflowTest.java`
**Comando para verificar:** `./mvnw test -Dtest=SearchAttributesWorkflowTest`

### C13-E06 — Correlación end-to-end
**Especificación:** Propagar correlationId y observar HTTP→Workflow→Activity.
**Criterios de Aceptación:** No depende de logs duplicados en replay; IDs consistentes.
**Archivos involucrados:** `CorrelacionWorkflow.java`, `CorrelacionActivity.java`
**Comando para verificar:** `./mvnw test -Dtest=CorrelacionWorkflowTest`

### C13-E07 — Configuración sin secretos
**Especificación:** Externalizar target, namespace y API key/mTLS; perfiles local/cloud.
**Criterios de Aceptación:** Ninguna clave real; TLS/API key configurables.
**Archivos involucrados:** `application.yml`, `.env.example`
**Comando para verificar:** Revisión manual de archivos.

### C13-E08 — History hygiene
**Especificación:** Revisar payloads, errores y Search Attributes para detectar datos excesivos.
**Criterios de Aceptación:** Propone redacción, referencia por ID o codec cuando corresponde.
**Archivos involucrados:** `audit.md`
**Comando para verificar:** Revisión manual del documento.

## Tareas para el Hogar

### C13-T01 — Suite Temporal completa
**Esfuerzo:** 60-90 min
**Especificación:** Agregar 20 pruebas con time skipping, fault injection, signals, updates, cancellation y replay.
**Criterios de Aceptación:** Mayoría integración; version de temporal-testing alineada con SDK.

### C13-T02 — Gate de replay
**Esfuerzo:** 60-90 min
**Especificación:** Crear comando CI que descargue/use historias de fixtures y ejecute replay.
**Criterios de Aceptación:** Falla ante nondeterminism y documenta actualización de fixtures.

### C13-T03 — Dashboard operativo
**Esfuerzo:** 60-90 min
**Especificación:** Definir métricas, SLO y consultas de visibilidad para workflows críticos.
**Criterios de Aceptación:** Incluye latencia, fallos, retries, task queue y pendientes.

### C13-T04 — Modelo de seguridad Temporal
**Esfuerzo:** 60-90 min
**Especificación:** Threat model de conexión, workers, payloads, secretos y acceso a UI/namespace.
**Criterios de Aceptación:** Mitigaciones priorizadas y responsabilidades claras.

## Cómo ejecutar

1. **Iniciar servidor Temporal local:**
   ```bash
   temporal server start-dev
   ```

2. **Ejecutar tests:**
   ```bash
   ./mvnw test
   ```

3. **Ejecutar aplicación Spring Boot:**
   ```bash
   ./mvnw spring-boot:run
   ```
