# Clase 03: Excepciones, logging, depuración y gestión de memoria

**Bloque:** Bloque 1 — Fundamentos de Java moderno  
**Duración:** 4 horas

## Objetivos de aprendizaje
- Distinguir errores recuperables, errores de programación y fallos de infraestructura.
- Diseñar excepciones personalizadas, preservar causas y usar try-with-resources.
- Aplicar logging estructurado con SLF4J/Logback y contexto de correlación.
- Usar breakpoints, watches, call stack, conditional breakpoints y análisis de stack traces.
- Explicar heap, stack, alcance, elegibilidad para GC y fugas por referencias retenidas.

## Cronograma de la clase

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Quiz y análisis de un stack trace | Pedir hipótesis antes de ejecutar el depurador. |
| 10–35 | Exposición de excepciones | Construir una taxonomía de errores del dominio. |
| 35–60 | Demo try-with-resources y logging | Mostrar causa, contexto y cierre automático. |
| 60–80 | Ejercicios E01–E03 | Revisar que no se traguen excepciones. |
| 80–95 | Receso | Preparar escenario de debugging. |
| 95–125 | Depuración guiada | Usar breakpoint de excepción y breakpoint condicional. |
| 125–160 | Laboratorio E04–E06 | Fault injection y corrección basada en evidencia. |
| 160–185 | Desafíos E07–E08 | Analizar memoria y logs con correlación. |
| 185–195 | Cierre y tarea | Entregar “postmortem” de cinco líneas. |

## Ejercicios de Clase

### C03-E01 — Parser robusto
**Especificación:** Procesar CSV de solicitudes y reportar número de línea, campo y causa sin detener todo el lote.  
**Criterios de aceptación:** Entradas válidas continúan; errores preservan causa y contexto.  
**Archivos involucrados:** `CsvParser.java`, `CsvParserTest.java`  
**Comando para verificar:** `./mvnw test -Dtest=CsvParserTest`

### C03-E02 — Importador seguro
**Especificación:** Leer archivo con BufferedReader y recurso simulado que puede fallar al cerrar.  
**Criterios de aceptación:** No hay cierre manual duplicado; recursos siempre cerrados.  
**Archivos involucrados:** `SafeImporter.java`, `SafeImporterTest.java`  
**Comando para verificar:** `./mvnw test -Dtest=SafeImporterTest`

### C03-E03 — Trazabilidad de operación
**Especificación:** Agregar SLF4J con correlationId, niveles y mensajes parametrizados.  
**Criterios de aceptación:** No loguea contraseñas/tokens; evita concatenación costosa.  
**Archivos involucrados:** `OperationTracker.java`, `OperationTrackerTest.java`, `logback.xml`  
**Comando para verificar:** `./mvnw test -Dtest=OperationTrackerTest`

### C03-E04 — Error intermitente por límite
**Especificación:** Depurar cálculo que falla solo para prioridad máxima y fin de mes.  
**Criterios de aceptación:** Incluye evidencia de variables inspeccionadas y prueba regresiva.  
**Archivos involucrados:** `PriorityCalculator.java`, `PriorityCalculatorTest.java`, `debug-notes.md`  
**Comando para verificar:** `./mvnw test -Dtest=PriorityCalculatorTest`

### C03-E05 — Transición inválida
**Especificación:** Crear InvalidStateTransitionException y traducir errores de bajo nivel a lenguaje del dominio.  
**Criterios de aceptación:** Causa original preservada cuando corresponde.  
**Archivos involucrados:** `InvalidStateTransitionException.java`, `StateService.java`, `StateServiceTest.java`  
**Comando para verificar:** `./mvnw test -Dtest=StateServiceTest`

### C03-E06 — Repositorio inestable
**Especificación:** Repositorio fake falla cada tercer llamado; servicio reacciona sin ocultar el fallo.  
**Criterios de aceptación:** No implementa reintento infinito; logs incluyen intento y operación.  
**Archivos involucrados:** `UnstableRepository.java`, `ResilientService.java`, `ResilientServiceTest.java`  
**Comando para verificar:** `./mvnw test -Dtest=ResilientServiceTest`

### C03-E07 — Fuga por listener
**Especificación:** Detectar objetos retenidos por listeners no removidos y corregir el ciclo de vida.  
**Criterios de aceptación:** Prueba demuestra que colección no crece indefinidamente.  
**Archivos involucrados:** `EventManager.java`, `EventManagerTest.java`  
**Comando para verificar:** `./mvnw test -Dtest=EventManagerTest`

### C03-E08 — Postmortem mínimo
**Especificación:** A partir de logs desordenados, reconstruir una operación y proponer 5 mejoras de logging.  
**Criterios de aceptación:** Diferencia hechos, hipótesis y acción preventiva.  
**Archivos involucrados:** `postmortem.md`  
**Comando para verificar:** Revisión manual del archivo Markdown.

## Tareas para el Hogar

### C03-T01 — Importador de lotes
**Esfuerzo:** 60-90 min  
**Especificación:** Importar solicitudes desde CSV con resumen de éxitos/fallos, errores de dominio y logging por lote.  
**Criterios de aceptación:** Ningún catch vacío; recursos cerrados; logs sin PII innecesaria.  
**Entregable:** Aplicación y 15 pruebas.

### C03-T02 — Laboratorio de debugging
**Esfuerzo:** 60-90 min  
**Especificación:** Resolver cuatro bugs entregados y documentar para cada uno síntoma, hipótesis, evidencia, causa y prueba regresiva.  
**Criterios de aceptación:** No se acepta solo “se corrigió”.  
**Entregable:** `docs/debug-lab.md` y commits separados.

### C03-T03 — Experimento de memoria
**Esfuerzo:** 60-90 min  
**Especificación:** Construir un programa que retenga referencias, observar crecimiento con herramienta del JDK y corregirlo.  
**Criterios de aceptación:** No afirmar que GC libera objetos alcanzables; resultados reproducibles.  
**Entregable:** Informe con capturas y explicación.

### C03-T04 — Política de errores y logs
**Esfuerzo:** 60-90 min  
**Especificación:** Redactar estándar de 1–2 páginas para el proyecto integrador.  
**Criterios de aceptación:** Incluye niveles, correlación, datos prohibidos y ejemplos.  
**Entregable:** `docs/error-logging-policy.md`.

## Cómo ejecutar

Para compilar y ejecutar todas las pruebas de los ejercicios:

```bash
cd ejercicios
./mvnw clean test
```

Para ejecutar un test específico:

```bash
cd ejercicios
./mvnw test -Dtest=NombreDelTest
```
