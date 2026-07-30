# Clase 18: Examen final teórico-práctico y defensa del proyecto

**Bloque:** Bloque 5 — Evaluación final  
**Duración:** 4 horas  

## Objetivos de Aprendizaje
- Demostrar conocimientos individuales de Java, Spring, seguridad, persistencia, Temporal y mensajería.
- Resolver una modificación práctica bajo tiempo limitado con pruebas.
- Demostrar resiliencia, trazabilidad y recuperación en el proyecto.
- Defender decisiones y reconocer limitaciones sin depender de IA para responder.

## Cronograma de la Clase

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Identificación y preparación | Verificar repositorios, ambiente y rama de examen. |
| 10–45 | Sección teórica individual | Preguntas de análisis, no memorización. |
| 45–115 | Práctico individual | Implementar cambio y corregir fallo con tests. |
| 115–125 | Pausa técnica | Guardar/commit; no continuar codificando. |
| 125–170 | Defensas y demostraciones | Turnos con falla controlada y preguntas. |
| 170–180 | Entrega final | Tag/commit hash, formulario y cierre. |

## Ejercicios de Clase

### C18-E01 — Análisis de arquitectura
**Especificación:** Responder 12 ítems: POO, transacción, seguridad, determinismo, timeouts, saga, messaging e IA.
**Entregable y aceptación:** Hoja individual. Justificaciones y detección de antipatrones; no solo definiciones.

### C18-E02 — Cambio de regla
**Especificación:** Agregar nueva regla de prioridad con validación, persistencia y contrato HTTP.
**Entregable y aceptación:** Commit de examen. Pruebas unitarias/web; migración compatible si aplica.

### C18-E03 — Nueva interacción
**Especificación:** Agregar Update o Signal con validación y prueba time-skipping.
**Entregable y aceptación:** Commit de examen. Determinismo y backward compatibility considerados.

### C18-E04 — Fallo inducido
**Especificación:** Corregir retry incorrecto o Activity no idempotente y añadir prueba regresiva.
**Entregable y aceptación:** Fix y postmortem corto. Causa raíz explicada; no esconder fallo.

### C18-E05 — Demo resiliente
**Especificación:** Demostrar happy path y un fallo recuperable/compensado.
**Entregable y aceptación:** Demo en vivo o video contingencia. Evidencia en UI/logs/DB; recuperación completa.

### C18-E06 — Preguntas técnicas
**Especificación:** Responder preguntas al azar y localizar código/prueba asociada.
**Entregable y aceptación:** Evaluación oral. Cada integrante comprende componentes principales.

### C18-E07 — Higiene de entrega
**Especificación:** Mostrar secret scan, dependencias, README, bootstrap y test report.
**Entregable y aceptación:** Checklist firmado. Sin secretos; build reproducible.

### C18-E08 — Limitaciones y roadmap
**Especificación:** Declarar 3 límites reales y 3 mejoras priorizadas.
**Entregable y aceptación:** Nota final. No presentar prototipo como producción sin reservas.

## Tareas para el Hogar

### C18-T01 — Corrección post-examen
**Esfuerzo:** 45-60 min
**Especificación:** Revisar feedback y proponer corrección de errores sin modificar entrega evaluada.
**Entregable y aceptación:** plan-mejora.md. Vincula error, causa, conocimiento faltante y acción.

### C18-T02 — Portafolio técnico
**Esfuerzo:** 60-90 min
**Especificación:** Preparar versión pública anonimizada o dossier interno del proyecto.
**Entregable y aceptación:** portfolio.md. Sin datos/secretos institucionales; incluye arquitectura y aprendizajes.

### C18-T03 — Autoevaluación
**Esfuerzo:** 30-45 min
**Especificación:** Comparar desempeño con rúbrica y aportar evidencia.
**Entregable y aceptación:** autoevaluacion.md. Honesta y específica.

### C18-T04 — Preparación de repetición
**Esfuerzo:** 60-120 min
**Especificación:** Solo para quien corresponda: plan de estudio focalizado y ejercicios de remediación.
**Entregable y aceptación:** plan-recuperacion.md. Prioriza brechas demostradas.

## Cómo ejecutar
Para esta clase de examen, el entorno se basa en el proyecto integrador.
1. Ejecutar tests: `./mvnw test`
2. Iniciar servicios (si aplica): `docker compose up -d`
3. Iniciar Temporal (si aplica): `temporal server start-dev`
4. Ejecutar aplicación: `./mvnw spring-boot:run`
