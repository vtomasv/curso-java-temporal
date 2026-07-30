# Clase 19: Examen de repetición, nivelación y cierre técnico

**Bloque:** Bloque 5 — Recuperación y cierre
**Duración:** 4 horas

## Objetivos de Aprendizaje
- Resolver una variante equivalente del examen sin reutilizar respuestas.
- Demostrar corrección de las brechas identificadas.
- Analizar errores comunes del curso mediante ejemplos anonimizados.
- Cerrar el portafolio y plan de aprendizaje posterior.

## Cronograma de la Clase

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Preparación y variante asignada | Verificar ambiente y repositorio limpio. |
| 10–45 | Teórico de repetición | Ítems equivalentes con código/casos distintos. |
| 45–115 | Práctico de repetición | Cambio funcional + resiliencia + pruebas. |
| 115–125 | Pausa/entrega parcial | Commit obligatorio. |
| 125–155 | Defensa individual | Demo y preguntas. |
| 155–175 | Clínica de errores comunes | Revisar ejemplos anonimizados y correcciones. |
| 175–180 | Cierre | Entregar plan de continuidad y encuesta. |

## Ejercicios de Clase

### C19-E01 — Análisis equivalente
**Especificación:** Resolver nueva matriz de 12 ítems con énfasis en brechas previas.
**Criterios de aceptación:** Mismo nivel cognitivo; casos distintos.
**Archivos involucrados:** `AnalisisEquivalente.java`
**Comando para verificar:** `./mvnw test -Dtest=AnalisisEquivalenteTest`

### C19-E02 — Cambio compatible
**Especificación:** Agregar atributo/consulta con migración, validación y seguridad.
**Criterios de aceptación:** No rompe datos ni permisos; tests.
**Archivos involucrados:** `CambioCompatible.java`
**Comando para verificar:** `./mvnw test -Dtest=CambioCompatibleTest`

### C19-E03 — Cancelación o compensación
**Especificación:** Implementar cancelación segura o nueva compensación idempotente.
**Criterios de aceptación:** No I/O en Workflow; comportamiento ante retry/cancel demostrado.
**Archivos involucrados:** `CancelacionCompensacion.java`
**Comando para verificar:** `./mvnw test -Dtest=CancelacionCompensacionTest`

### C19-E04 — Nondeterminism o duplicate effect
**Especificación:** Diagnosticar y corregir uno de dos fallos asignados.
**Criterios de aceptación:** Causa raíz y prevención.
**Archivos involucrados:** `Nondeterminism.java`
**Comando para verificar:** `./mvnw test -Dtest=NondeterminismTest`

### C19-E05 — Demostración técnica
**Especificación:** Mostrar cambio, tests y fallo recuperado.
**Criterios de aceptación:** Evidencia directa y explicación clara.
**Archivos involucrados:** Defensa oral.

### C19-E06 — Errores comunes
**Especificación:** Corregir en grupo tres snippets: catch vacío, @Transactional mal ubicado y HTTP en Workflow.
**Criterios de aceptación:** Explica por qué, no solo corrige.
**Archivos involucrados:** `ErroresComunes.java`
**Comando para verificar:** `./mvnw test -Dtest=ErroresComunesTest`

### C19-E07 — Upgrade rehearsal
**Especificación:** Simular actualización de dependencia, ejecutar tests/replay y registrar riesgos.
**Criterios de aceptación:** No actualizar a ciegas; rollback plan.
**Archivos involucrados:** `pom.xml`

### C19-E08 — Ficha de proyecto
**Especificación:** Redactar resumen técnico de una página con contribución individual.
**Criterios de aceptación:** Sin datos sensibles; enlaces/evidencias internas válidas.
**Archivos involucrados:** `portfolio-one-pager.md`

## Tareas para el Hogar

### C19-T01 — Plan 30-60-90
**Esfuerzo:** 30-45 min
**Especificación:** Definir práctica técnica para 30, 60 y 90 días.
**Entregable:** `learning-plan.md`
**Criterios:** Metas medibles y repositorios/proyectos concretos.

### C19-T02 — Backlog de mantenimiento
**Esfuerzo:** 45-60 min
**Especificación:** Crear 10 issues futuros priorizados por riesgo/valor.
**Entregable:** `maintenance-backlog.md`
**Criterios:** Incluye dependencias, seguridad, observabilidad y deuda.

### C19-T03 — Lecciones aprendidas
**Esfuerzo:** 30-45 min
**Especificación:** Escribir postmortem del proceso de aprendizaje y 5 prácticas que mantendrá.
**Entregable:** `retrospectiva.md`
**Criterios:** Ejemplos concretos del curso.

### C19-T04 — Contribución final
**Esfuerzo:** 30-60 min
**Especificación:** Corregir una mejora documental o prueba en el repositorio común, si la política institucional lo permite.
**Entregable:** Pull request.
**Criterios:** Cambio pequeño, revisable y sin respuestas de examen.

## Cómo ejecutar

Para ejecutar los tests de los ejercicios:
```bash
cd ejercicios
./mvnw clean test
```

Para levantar la base de datos y Temporal (si aplica):
```bash
docker compose up -d
temporal server start-dev
```
