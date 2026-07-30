# Clase 04: Collections, Streams, E/S, redes, concurrencia y pruebas básicas

**Bloque:** Bloque 1 — Fundamentos de Java moderno  
**Duración:** 4 horas  

## Objetivos de Aprendizaje
- Seleccionar List, Set, Map, Queue y colecciones inmutables según semántica.
- Usar Streams para filtrar, transformar, agrupar y reducir sin efectos secundarios ocultos.
- Aplicar NIO.2 y HttpClient para archivos y red.
- Distinguir concurrencia bloqueante, CompletableFuture y virtual threads.
- Escribir pruebas JUnit 5/AssertJ y JavaDoc/README que se mantengan con el código.

## Cronograma de la Clase

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Quiz de selección de colecciones | Cinco escenarios; justificar estructura. |
| 10–35 | Collections y generics | Mostrar contratos y errores de mutabilidad. |
| 35–60 | Streams en live coding | Construir pipeline y medir claridad, no solo brevedad. |
| 60–80 | Ejercicios E01–E03 | Procesamiento de datos con pruebas. |
| 80–95 | Receso | Preparar servidor HTTP local stub. |
| 95–120 | E/S, HttpClient y timeouts | Demo de red con fallo controlado. |
| 120–160 | Concurrencia y pruebas E04–E06 | Comparar pool fijo y virtual threads. |
| 160–185 | Desafíos E07–E08 | Documentación viva y refactor de pipeline. |
| 185–195 | Cierre y tarea | Ticket: regla para elegir Set vs List y límite dentro de Workflow. |

## Ejercicios de Clase

### C04-E01 — Deduplicación de solicitudes
- **Especificación:** Eliminar duplicados por identificador conservando el orden de llegada.
- **Archivos involucrados:** `Solicitud.java`, `Deduplicador.java`, `DeduplicadorTest.java`
- **Criterio de aceptación:** Semántica de igualdad explícita; orden preservado.
- **Comando para verificar:** `./mvnw test -Dtest=DeduplicadorTest`

### C04-E02 — Índice por responsable
- **Especificación:** Construir `Map<Responsable,List<Solicitud>>` y consultar sin NPE.
- **Archivos involucrados:** `Responsable.java`, `IndiceResponsables.java`, `IndiceResponsablesTest.java`
- **Criterio de aceptación:** Colecciones devueltas no modificables o defensivas.
- **Comando para verificar:** `./mvnw test -Dtest=IndiceResponsablesTest`

### C04-E03 — Tablero de métricas
- **Especificación:** Agrupar por estado y prioridad; calcular conteo y promedio de horas.
- **Archivos involucrados:** `MetricasDTO.java`, `TableroMetricas.java`, `TableroMetricasTest.java`
- **Criterio de aceptación:** Pipeline sin side effects; nombres intermedios legibles.
- **Comando para verificar:** `./mvnw test -Dtest=TableroMetricasTest`

### C04-E04 — Exportación atómica
- **Especificación:** Escribir reporte a archivo temporal y moverlo al destino al finalizar.
- **Archivos involucrados:** `Exportador.java`, `ExportadorTest.java`
- **Criterio de aceptación:** Charset explícito; no deja archivo parcial ante fallo.
- **Comando para verificar:** `./mvnw test -Dtest=ExportadorTest`

### C04-E05 — Cliente de catálogo
- **Especificación:** Consumir endpoint stub con HttpClient, timeout y manejo de 2xx/4xx/5xx.
- **Archivos involucrados:** `ClienteCatalogo.java`, `ClienteCatalogoTest.java`
- **Criterio de aceptación:** No bloquea indefinidamente; error contiene status y URI segura.
- **Comando para verificar:** `./mvnw test -Dtest=ClienteCatalogoTest`

### C04-E06 — Procesamiento paralelo seguro
- **Especificación:** Procesar 100 solicitudes con virtual threads y acumular resultados sin carrera.
- **Archivos involucrados:** `ProcesadorParalelo.java`, `ProcesadorParaleloTest.java`
- **Criterio de aceptación:** Resultados deterministas; recursos cerrados; sin shared mutable state inseguro.
- **Comando para verificar:** `./mvnw test -Dtest=ProcesadorParaleloTest`

### C04-E07 — Matriz parametrizada
- **Especificación:** Pruebas parametrizadas para reglas de prioridad, estado y SLA.
- **Archivos involucrados:** `ReglasNegocio.java`, `ReglasNegocioTest.java`
- **Criterio de aceptación:** ParameterizedTest con al menos 12 casos. Nombres de casos legibles y límites cubiertos.
- **Comando para verificar:** `./mvnw test -Dtest=ReglasNegocioTest`

### C04-E08 — README ejecutable
- **Especificación:** Documentar cómo ejecutar, probar y reproducir un fallo; incluir ejemplo curl futuro.
- **Archivos involucrados:** `README.md` (de este proyecto), `Documentacion.java`
- **Criterio de aceptación:** Comandos copiados funcionan; documentación coincide con código.
- **Comando para verificar:** Revisión manual del README y JavaDoc.

## Tareas para el Hogar

### C04-T01 — Analizador de bitácoras
- **Esfuerzo:** 60-90 min
- **Especificación:** Leer archivo grande, filtrar por correlationId, agrupar errores y generar reporte JSON.
- **Entregable:** Módulo log-analyzer.
- **Criterios:** Streaming de archivo; no cargar todo innecesariamente; 20 pruebas.

### C04-T02 — Agregador HTTP concurrente
- **Esfuerzo:** 60-90 min
- **Especificación:** Consultar tres servicios stub en paralelo con timeout global y resultado parcial.
- **Entregable:** Cliente y pruebas de fallos/latencia.
- **Criterios:** No pierde causa; limita concurrencia y documenta estrategia.

### C04-T03 — Colecciones y rendimiento
- **Esfuerzo:** 60-90 min
- **Especificación:** Comparar List/Set/Map en escenarios definidos y explicar resultados sin generalizaciones absolutas.
- **Entregable:** Informe y benchmark simple o medición controlada.
- **Criterios:** Incluye warm-up y limitaciones del experimento.

### C04-T04 — Contrato de pruebas
- **Esfuerzo:** 60-90 min
- **Especificación:** Definir pirámide de pruebas y convención de nombres para el curso.
- **Entregable:** docs/testing-strategy.md.
- **Criterios:** Incluye unidad, integración y futura prueba Temporal.

## Cómo ejecutar

Para compilar y ejecutar las pruebas de todos los ejercicios:

```bash
cd ejercicios
./mvnw clean test
```

Para ejecutar un test específico:

```bash
cd ejercicios
./mvnw test -Dtest=NombreDelTest
```
