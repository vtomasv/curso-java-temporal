# Clase 17: Proyecto integrador: implementación, hardening y ensayo de defensa

**Bloque:** Bloque 5 — Integración final
**Duración:** 4 horas

## Objetivos de Aprendizaje
- Completar ruta crítica end-to-end con seguridad y persistencia.
- Ejecutar pruebas de carga ligera, caos, seguridad, replay y recuperación.
- Resolver deuda crítica sin reescrituras tardías.
- Preparar demo reproducible con fallos controlados y observabilidad.
- Defender decisiones arquitectónicas y limitaciones.

## Cronograma de la Clase

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–15 | Stand-up y gate de build | Cada equipo demuestra bootstrap y tests. |
| 15–40 | Triage técnico | Priorizar fallos que impiden demo o violan invariantes. |
| 40–80 | Sprint guiado A | Docente hace code review focalizado. |
| 80–95 | Receso | Congelar cambios no críticos. |
| 95–125 | Chaos/security/replay | Ejecutar batería común. |
| 125–160 | Sprint guiado B | Corregir P0/P1 y actualizar runbook. |
| 160–185 | Ensayo de demo/defensa | 5 min demo + 5 min preguntas por equipo. |
| 185–195 | Release candidate | Etiquetar versión y checklist de examen. |

## Ejercicios de Clase

### C17-E01 — Bootstrap desde cero
**Especificación:** Clonar en directorio limpio y levantar app, DB, Temporal y broker.
**Criterios de Aceptación:** Máximo 15 min; un comando o pasos claros; sin estado oculto.
**Archivos involucrados:** `docker-compose.yml`, `README.md` del proyecto.
**Comando para verificar:** `./mvnw clean install` y `docker compose up -d`

### C17-E02 — Ruta crítica
**Especificación:** Crear solicitud, autenticar, iniciar saga, aprobar, notificar y consultar.
**Criterios de Aceptación:** Resultado verificable en API, DB, Temporal UI y broker.
**Archivos involucrados:** `EndToEndIT.java`
**Comando para verificar:** `./mvnw test -Dtest=EndToEndIT`

### C17-E03 — Fallo de proveedor
**Especificación:** Detener servicio/Activity dependiente durante operación y recuperarlo.
**Criterios de Aceptación:** Workflow no se pierde; retry/compensación coherente.
**Archivos involucrados:** `ResilienceIT.java`
**Comando para verificar:** `./mvnw test -Dtest=ResilienceIT`

### C17-E04 — Compatibilidad de release
**Especificación:** Ejecutar replay de historias guardadas con código final.
**Criterios de Aceptación:** Cero nondeterminism o estrategia versionada documentada.
**Archivos involucrados:** `WorkflowReplayTest.java`
**Comando para verificar:** `./mvnw test -Dtest=WorkflowReplayTest`

### C17-E05 — Matriz de ataque
**Especificación:** Ejecutar tokens inválidos, IDOR, mass assignment, secret scan y logs.
**Criterios de Aceptación:** Sin vulnerabilidad crítica abierta; excepciones justificadas.
**Archivos involucrados:** `SecurityRegressionIT.java`
**Comando para verificar:** `./mvnw test -Dtest=SecurityRegressionIT`

### C17-E06 — Redelivery/DLQ
**Especificación:** Duplicar evento y enviar poison message durante demo.
**Criterios de Aceptación:** No duplicación; DLQ y recuperación operativa.
**Archivos involucrados:** `MessagingResilienceIT.java`
**Comando para verificar:** `./mvnw test -Dtest=MessagingResilienceIT`

### C17-E07 — Diagnóstico en 5 minutos
**Especificación:** A partir de alerta simulada, localizar workflow/activity/mensaje y causa.
**Criterios de Aceptación:** Usa correlationId, métricas/historia; no inspección manual aleatoria.
**Archivos involucrados:** `ObservabilityIT.java`
**Comando para verificar:** `./mvnw test -Dtest=ObservabilityIT`

### C17-E08 — Preguntas hostiles
**Especificación:** Responder trade-offs: Java 25, Boot 4, Temporal, saga, broker, IA y seguridad.
**Criterios de Aceptación:** Respuesta con evidencia de código/prueba y limitaciones honestas.
**Archivos involucrados:** `docs/defensa.md`
**Comando para verificar:** Revisión manual del documento.

## Tareas para el Hogar

### C17-T01 — Release final
**Especificación:** Cerrar P0/P1, etiquetar v1.0.0 y generar paquete de entrega.
**Criterios de Aceptación:** Build reproducible; tests verdes; no secretos.

### C17-T02 — Video de contingencia
**Especificación:** Grabar demo de 8–10 min por si falla infraestructura el día del examen.
**Criterios de Aceptación:** Incluye fallo/resiliencia, no solo happy path.

### C17-T03 — Informe técnico
**Especificación:** Resumen de arquitectura, decisiones, pruebas, riesgos y trabajo futuro.
**Criterios de Aceptación:** Coherente con repositorio; evidencia enlazada.

### C17-T04 — Preparación individual
**Especificación:** Banco de 30 preguntas y respuestas breves; cada integrante domina todo el sistema.
**Criterios de Aceptación:** No repartir conocimiento en silos; respuestas verificables.

## Cómo ejecutar

1. **Levantar infraestructura (Base de datos, Temporal, RabbitMQ):**
   ```bash
   docker compose up -d
   ```
   *(Alternativamente, para Temporal local: `temporal server start-dev`)*

2. **Ejecutar tests:**
   ```bash
   ./mvnw test
   ```

3. **Ejecutar aplicación:**
   ```bash
   ./mvnw spring-boot:run
   ```
