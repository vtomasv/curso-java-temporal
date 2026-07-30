# Planificación detallada de 76 horas presenciales: Desarrollo de Aplicaciones con Java 25 y Temporal.io

**19 sesiones de 4 horas pedagógicas — programa, guion docente, ejercicios de repositorio y tareas**

## 1. Marco de la planificación

La programación institucional establece 76 horas, distribuidas en 19 instancias de 4 horas pedagógicas. Los horarios de 08:00–11:15 y 13:45–17:00 equivalen a 180 minutos de docencia más 15 minutos de receso. Las sesiones de examen se planifican sobre 180 minutos. La visita profesional del 26 al 30 de octubre se utiliza como actividad de observación entre las clases 14 y 15, sin sumarla a las 76 horas.

### Línea tecnológica de referencia

- JDK 25 LTS como versión base del curso. Se evita basar el curso en Java 26 por ser una versión no LTS.
- Spring Boot 4.1.x y Maven Wrapper. El repositorio debe centralizar versiones y permitir actualización controlada.
- Temporal Java SDK 1.37.0 como línea base verificada al 29-07-2026; todas las dependencias Temporal deben usar la misma versión.
- Temporal CLI y servidor de desarrollo local; Temporal Cloud solo como demostración/configuración opcional.
- PostgreSQL en Docker Compose, Flyway, Spring Data JPA, Spring Security y Thymeleaf.
- RabbitMQ con Spring AMQP para el laboratorio de colas; Kafka/JMS se tratan comparativamente.
- Spring AI para integración de modelos; las llamadas LLM se ejecutan en Activities. La integración temporal-spring-ai se presenta como vista previa y no como dependencia obligatoria del proyecto.
- VS Code como entorno principal: Extension Pack for Java, Spring Boot Extension Pack, Docker, REST Client y Git.

### Reglas obligatorias de diseño Temporal

- El Workflow contiene orquestación determinista; toda llamada HTTP, base de datos, archivo, broker, modelo de IA o efecto externo se implementa como Activity.
- Dentro de Workflow usar Workflow.currentTimeMillis(), Workflow.sleep(), Async/Promise y colecciones/concurrencia de Temporal; no usar Instant.now(), UUID.randomUUID(), Thread, ExecutorService o CompletableFuture nativos.
- Toda Activity debe declarar timeouts; toda política de retry debe tener justificación, límite y clasificación de errores no reintentables.
- Las Activities con efectos deben ser idempotentes y tolerar redelivery/retry; utilizar claves de negocio, restricciones únicas o inbox/deduplicación.
- Las Activities largas deben emitir heartbeats y poder reanudar progreso; la cancelación debe ser cooperativa.
- Workflow IDs deben ser estables y significativos para el negocio; commandId/messageId para deduplicar Signals/Updates/eventos.
- Mantener Event History acotada: payloads pequeños, referencias por ID, Continue-As-New cuando corresponda y datos sensibles minimizados.
- Cambios de Workflow deben pasar replay testing y una estrategia de versionado/patching antes del despliegue.
- Preferir cancelación sobre terminación; la terminación se reserva para operaciones atascadas que no pueden cancelarse limpiamente.
- La mayoría de las pruebas Temporal deben ser de integración con TestWorkflowEnvironment y time skipping, complementadas con replay de historias.

## 2. Resultado integrador del curso

El hilo conductor es **SIGEO — Sistema Integrado de Gestión de Solicitudes y Operaciones**. Evoluciona desde una aplicación de consola hasta una solución web segura con persistencia, Workflow/Saga durable, colas de mensajes e integración de IA opcional. El producto final debe poder reproducir fallos, recuperarse y explicar su estado mediante pruebas y observabilidad.

## 3. Metodología presencial

Cada clase combina activación, exposición breve, demostración en vivo, laboratorio guiado, práctica independiente, desafío para alumnos rápidos, revisión por pares y ticket de salida. El banco de ejercicios contiene más actividades que las estrictamente necesarias: el docente aplica las obligatorias y libera extensiones según velocidad. La tarea se diseña para 4–6 horas semanales en las clases de mayor carga y 2–4 horas en las restantes.

## 4. Repositorio de ejercicios

```text
curso-java-temporal/
├── README.md
├── pom.xml                         # agregador y propiedades de versiones
├── .mvn/wrapper/                   # Maven Wrapper
├── compose.yaml                    # PostgreSQL, RabbitMQ y servicios stub
├── docs/
│   ├── programa/
│   ├── adr/
│   ├── arquitectura/
│   ├── runbooks/
│   └── rubricas/
├── class-01-java-ecosystem/
│   ├── README.md                   # objetivos, orden y tiempos
│   ├── exercises/
│   │   ├── c01-e01-env-audit/
│   │   │   ├── starter/
│   │   │   ├── README.md
│   │   │   └── tests/
│   │   └── ...
│   ├── homework/
│   └── instructor-solutions/       # rama o repo privado
├── class-02-oop/
├── ...
├── class-17-project-hardening/
├── class-18-final-exam/             # acceso restringido
└── class-19-recovery/               # acceso restringido
```

- Cada ejercicio tiene README con contexto, precondiciones, pasos, restricciones, ejemplos de entrada/salida y criterios de aceptación.
- Cada carpeta starter compila antes de comenzar; los TODO están numerados y no incluyen la solución.
- Las pruebas públicas orientan el contrato; pruebas adicionales del docente verifican bordes y antipatrones.
- Las soluciones del profesor se mantienen en repositorio privado o rama protegida y se publican después del cierre.
- Cada ejercicio se entrega en un commit con formato Cxx-Eyy: descripción; la tarea usa Cxx-Tyy.
- No incluir claves, tokens, certificados, archivos .env reales ni Event Histories con datos sensibles.
- El pipeline mínimo ejecuta mvnw verify, pruebas de arquitectura, secret scan y, desde clase 13, replay tests.
- Las dependencias se controlan en un BOM/properties y se actualizan en pull requests separados con evidencia de pruebas.

## 5. Evaluación sugerida

| Componente | Peso | Evidencia |
|---|---:|---|
| Ejercicios y evidencias de clase | 25% | Commits, pruebas, ticket de salida y explicación oral. |
| Tareas y bitácora técnica | 20% | Calidad, reproducibilidad, revisión crítica de IA y puntualidad. |
| Controles prácticos/bloques | 15% | Katas, debugging, seguridad y Temporal. |
| Proyecto integrador | 25% | Arquitectura, funcionalidad, resiliencia, seguridad, pruebas y defensa. |
| Examen final individual | 15% | Teórico-práctico y capacidad de diagnóstico/explicación. |

### Rúbrica transversal

- **Correctitud funcional:** Cumple casos y bordes; reglas de negocio explícitas; errores controlados.
- **Diseño y mantenibilidad:** Responsabilidades claras, nombres, encapsulación, bajo acoplamiento y documentación útil.
- **Pruebas:** Pruebas relevantes por capa; integración real donde corresponde; replay y fault injection en Temporal.
- **Resiliencia:** Timeouts, retry limitado, idempotencia, cancelación, compensación, DLQ y recuperación.
- **Seguridad:** Autenticación/autorización, validación, secretos, datos sensibles y pruebas negativas.
- **Operabilidad:** Logs correlacionados, métricas, health, Search Attributes, runbook y diagnóstico.
- **Uso responsable de IA:** Prompts registrados, código comprendido, pruebas añadidas, privacidad y límites declarados.
- **Comunicación:** README reproducible, diagramas coherentes y defensa técnica basada en evidencia.

## 6. Planificación clase a clase


---

# Clase 1: Java 25 LTS, ecosistema Java y entorno profesional

**Fecha:** 23-07-2026  
**Bloque:** Bloque 1 — Fundamentos de Java moderno

## Propósito
Establecer un entorno reproducible, diferenciar Java SE, Jakarta EE y Spring Boot, y dominar el ciclo editar–compilar–probar–ejecutar en VS Code.

## Resultados de aprendizaje

- Instalar y verificar JDK 25 LTS, Git, Docker, Maven Wrapper y extensiones de Java para VS Code.
- Explicar bytecode, JVM, JDK, classpath, módulos y empaquetado JAR/WAR a nivel introductorio.
- Escribir programas con variables, tipos, operadores, control de flujo, métodos y entrada por consola.
- Comparar el despliegue en servidores Jakarta EE open source (WildFly, Open Liberty, Payara) con aplicaciones Spring Boot autocontenidas.
- Usar IA como asistente verificable: pedir, revisar, ejecutar pruebas y registrar el prompt utilizado.

## Desarrollo de los temas

- Java SE 25 y política LTS; compilación a bytecode y portabilidad de la JVM.
- JDK, JRE conceptual, Maven, estructura estándar de proyecto y Maven Wrapper.
- Sintaxis esencial: tipos primitivos, String, conversiones, operadores, if/switch, bucles y métodos.
- Java moderno: inferencia local con var, switch expressions y records como vista previa conceptual.
- Jakarta EE: contenedor, CDI, JAX-RS, JPA, WAR; servidores open source y comparación con Spring Boot.
- VS Code: terminal, tareas, ejecución, breakpoints básicos y control de versiones con Git.
- Normas de IA: no integrar código que el alumno no pueda explicar; exigir tests y referencias.

## Guion de presentación

- 1. Portada, resultados del curso y producto integrador SIGEO.
- 2. Mapa Java SE → Jakarta EE/Spring → Temporal → mensajería/IA.
- 3. JDK/JVM/bytecode mediante un diagrama de compilación y ejecución.
- 4. Demostración: crear repositorio, Maven Wrapper y primer commit.
- 5. Sintaxis mínima con live coding; mostrar errores de compilación deliberados.
- 6. Comparación de un WAR en servidor de aplicaciones y un JAR ejecutable.
- 7. Protocolo de trabajo con IA: prompt, diff, prueba, explicación y commit.
- 8. Presentación de ejercicios, criterios y ticket de salida.

## Secuencia temporal

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Diagnóstico rápido y presentación del proyecto integrador | Aplicar 8 preguntas; identificar experiencia previa y restricciones de equipos. |
| 10–35 | Exposición: ecosistema Java y versiones | Dibujar el flujo fuente→bytecode→JVM; comparar SE/Jakarta EE/Spring. |
| 35–55 | Demostración de instalación y proyecto Maven | Verificar java --version, ./mvnw -v, ejecución y depuración. |
| 55–80 | Microejercicios E01–E03 | Circular, exigir ejecución desde terminal y explicación del código. |
| 80–95 | Receso | Resolver incidencias de instalación sin detener al grupo. |
| 95–120 | Sintaxis y control de flujo | Live coding incremental; introducir switch expression. |
| 120–160 | Laboratorio E04–E06 | Usar pruebas de aceptación simples y revisión por pares. |
| 160–185 | Desafíos E07–E08 | Asignar extensiones a quienes terminen antes; no dar la solución completa. |
| 185–195 | Cierre y tarea | Ticket: explicar JDK/JVM/JAR y mostrar un commit firmado por el alumno. |

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C01-E01 — Auditoría del entorno | Setup verificable, 10 min | Crear un script o documento que capture versiones de Java, Maven Wrapper, Git y Docker, y valide que el proyecto compila. | **Entregable:** env-report.md y salida de ./mvnw test. **Criterios:** Todas las herramientas verificadas; sin rutas personales ni secretos. |
| C01-E02 — Conversor de unidades | Micro-kata, 12 min | Aplicación de consola que convierta temperatura y distancia, con validación de opción y formato de salida estable. | **Entregable:** Main.java y tres ejecuciones de ejemplo. **Criterios:** Cubre entradas válidas, opción desconocida y redondeo definido. |
| C01-E03 — Clasificador de prioridad | Micro-kata, 12 min | Usar switch expression para convertir un código 1–5 en prioridad y plazo máximo. | **Entregable:** Método priorityFor(int) y pruebas parametrizadas mínimas. **Criterios:** No usar cadena de if; lanza error controlado fuera de rango. |
| C01-E04 — Solicitud operativa | Modelado inicial, 20 min | Definir un record Solicitud con id, solicitante, descripción, prioridad y fecha; imprimir resumen legible. | **Entregable:** Record, factoría y demo. **Criterios:** Inmutabilidad; validación de campos obligatorios. |
| C01-E05 — De script a métodos | Refactor, 20 min | Recibir un programa monolítico y separar lectura, validación, cálculo y presentación. | **Entregable:** Commit antes/después y explicación de responsabilidades. **Criterios:** Métodos pequeños; no duplicación; comportamiento preservado. |
| C01-E06 — Menú SIGEO v0 | CLI, 25 min | Construir menú alta/listado/búsqueda/salida usando una lista en memoria y bucle de aplicación. | **Entregable:** Proyecto ejecutable con datos de ejemplo. **Criterios:** No termina ante entrada inválida; salida consistente. |
| C01-E07 — Empaquetado reproducible | Desafío rápido, 18 min | Configurar manifest y generar JAR ejecutable; ejecutar fuera del IDE. | **Entregable:** JAR y comando documentado. **Criterios:** Funciona desde terminal limpia usando Maven Wrapper. |
| C01-E08 — Auditor de código generado | Revisión con IA, 18 min | Pedir a una IA dos soluciones al mismo problema, comparar legibilidad, errores y complejidad, y elegir una con argumentos. | **Entregable:** ia-review.md con prompt, diferencias y decisión. **Criterios:** Incluye al menos un defecto detectado y una prueba añadida para cubrirlo. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C01-T01 — Calculadora de plazos | 60-90 min | CLI con alta de fecha y prioridad que calcule fecha límite, evitando duplicación de lógica. | **Entregable:** Proyecto Maven y README con casos. **Criterios:** Al menos 8 pruebas, incluyendo límites de mes. |
| C01-T02 — Mapa del ecosistema | 60-90 min | Crear diagrama Mermaid que relacione Java SE, Jakarta EE, Spring Boot, servidor de aplicaciones, base de datos, Temporal y broker. | **Entregable:** docs/ecosistema.md. **Criterios:** Flechas y responsabilidades correctas; máximo una página. |
| C01-T03 — Guía de instalación del equipo | 60-90 min | Documentar instalación limpia en Windows o Linux con comandos de verificación y solución de 3 fallos frecuentes. | **Entregable:** docs/setup.md. **Criterios:** Reproducible por un compañero. |
| C01-T04 — Bitácora de IA | 60-90 min | Resolver una kata adicional con IA y registrar prompt inicial, correcciones, pruebas y reflexión. | **Entregable:** docs/ia-log-c01.md. **Criterios:** No se acepta solo código final; debe mostrar validación crítica. |

## Evidencia de salida
El alumno ejecuta el JAR, muestra el historial Git y explica con sus palabras la diferencia entre JDK, JVM, JAR y servidor de aplicaciones.

## Preparación del docente
Preinstalar JDK 25 LTS, Git y Docker; preparar un repositorio plantilla con Maven Wrapper, JUnit y carpeta docs. Llevar capturas de WildFly/Open Liberty/Payara solo para comparación conceptual.


---

# Clase 2: Programación orientada a objetos moderna y diseño mantenible

**Fecha:** 30-07-2026  
**Bloque:** Bloque 1 — Fundamentos de Java moderno

## Propósito
Construir un modelo de dominio consistente con encapsulación, composición, herencia limitada, interfaces, records y principios SOLID básicos.

## Resultados de aprendizaje

- Diseñar clases con invariantes, constructores y métodos de comportamiento, evitando modelos anémicos.
- Distinguir identidad, igualdad, mutabilidad e inmutabilidad.
- Aplicar composición, interfaces y polimorfismo antes de recurrir a herencia.
- Usar enums, records, sealed classes y pattern matching cuando agreguen claridad.
- Refactorizar código guiándose por pruebas y responsabilidades.

## Desarrollo de los temas

- Clase, objeto, referencia, estado, comportamiento, constructor, modificadores y paquetes.
- Encapsulación e invariantes; this, static, final y visibilidad mínima.
- equals/hashCode/toString; identidad de entidad frente a objeto valor.
- Interfaces, clases abstractas, composición y polimorfismo.
- Records para datos inmutables; enums con comportamiento; sealed hierarchies y pattern matching.
- SOLID introductorio: SRP, OCP, DIP y olor de clase “Dios”.
- Clases internas: miembro, estática, local y anónima; cuándo evitarlas.

## Guion de presentación

- 1. Del problema al modelo: sustantivos no siempre son clases.
- 2. Invariantes como contrato del objeto.
- 3. Demostración: Entidad Solicitud y objetos valor Prioridad/Correo.
- 4. Igualdad y colecciones: por qué equals/hashCode importan.
- 5. Composición vs. herencia con contraejemplo.
- 6. Records, enums y sealed classes en Java 25.
- 7. Clases internas y lambdas: alcance y captura.
- 8. Refactor guiado por pruebas y checklist SOLID.

## Secuencia temporal

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Recuperación y quiz de conceptos | Revisar tarea de setup y tres errores comunes. |
| 10–35 | Exposición POO e invariantes | Modelar en pizarra una solicitud con estados válidos. |
| 35–60 | Live coding de objetos valor y entidad | Mostrar constructor protegido y métodos de transición. |
| 60–80 | Ejercicios E01–E03 | Pair programming con rotación de conductor. |
| 80–95 | Receso | Revisar commits parciales. |
| 95–120 | Polimorfismo, composición y Java moderno | Demostrar sealed interface y pattern matching. |
| 120–160 | Laboratorio E04–E06 | Exigir pruebas antes de cada refactor. |
| 160–185 | Desafíos E07–E08 | Asignar code review cruzado con checklist. |
| 185–195 | Cierre y tarea | Exit ticket: justificar una decisión composición/herencia. |

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C02-E01 — Invariante de Recurso | Diseño de clase, 15 min | Crear Recurso con código, nombre y estado; impedir código vacío y cantidades negativas. | **Entregable:** Clase y pruebas de constructor. **Criterios:** No expone setters generales; errores con mensajes útiles. |
| C02-E02 — CorreoInstitucional | Objeto valor, 15 min | Modelar correo inmutable, normalizar dominio y comparar por valor. | **Entregable:** Record o clase final con tests. **Criterios:** equals/hashCode correctos; no acepta formato inválido. |
| C02-E03 — Prioridad y SLA | Enum con comportamiento, 15 min | Enum que conozca horas de atención y factor de escalamiento. | **Entregable:** Enum, método deadlineFrom y tests. **Criterios:** Sin switch externo duplicado para obtener SLA. |
| C02-E04 — Canales de notificación | Polimorfismo, 25 min | Interfaz Notificador con implementaciones consola, correo simulado y SMS simulado; servicio depende de interfaz. | **Entregable:** Tres implementaciones y prueba con fake. **Criterios:** DIP: servicio no instancia implementaciones concretas. |
| C02-E05 — Eliminar herencia frágil | Refactor, 25 min | Transformar jerarquía Recurso→Vehículo→Ambulancia con flags en composición de capacidades. | **Entregable:** Diff y ADR corto. **Criterios:** Se elimina comportamiento condicional por tipo cuando es posible. |
| C02-E06 — Estados sellados | Java moderno, 25 min | Modelar estados Borrador, EnRevisión, Aprobada, Rechazada con sealed interface y pattern matching. | **Entregable:** Jerarquía y función de presentación exhaustiva. **Criterios:** Compilador obliga a tratar todos los estados. |
| C02-E07 — Validador contextual | Clase interna, 18 min | Implementar clase interna privada que valide una transición usando estado del agregado. | **Entregable:** Código y nota de cuándo la clase interna es apropiada. **Criterios:** No expone la clase; acceso justificado al contexto externo. |
| C02-E08 — Cazador de olores POO | Code review, 18 min | Revisar una clase de 200 líneas e identificar al menos 8 olores, priorizar 3 y aplicar 2 refactors seguros. | **Entregable:** review.md y commits pequeños. **Criterios:** Cada refactor respaldado por pruebas. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C02-T01 — Modelo SIGEO v1 | 60-90 min | Modelar Solicitud, Solicitante, Recurso, Aprobación y Estado con relaciones y reglas de transición. | **Entregable:** Módulo domain con 20 pruebas. **Criterios:** Sin setters indiscriminados; cobertura de estados inválidos. |
| C02-T02 — Catálogo de patrones POO | 60-90 min | Crear ejemplos mínimos de estrategia, fábrica y adaptador dentro del dominio. | **Entregable:** Tres paquetes con README comparativo. **Criterios:** Cada patrón resuelve un problema real, no ceremonial. |
| C02-T03 — Refactor de código legado | 60-90 min | Recibir una solución procedural y convertirla a un diseño orientado a objetos en 4 commits. | **Entregable:** Rama refactor/c02. **Criterios:** Pruebas preservan comportamiento; explicación del diseño. |
| C02-T04 — Defensa oral grabada | 45-60 min | Grabar 5 minutos explicando invariantes, igualdad y por qué eligió composición o herencia. | **Entregable:** Enlace o guion en docs. **Criterios:** Debe referirse a su propio código y a una prueba concreta. |

## Evidencia de salida
Mapa de clases actualizado, suite verde y explicación de una invariante protegida por el modelo.

## Preparación del docente
Preparar código legado con herencia frágil, tarjetas de estados y un conjunto de pruebas que permitan refactor seguro.


---

# Clase 3: Excepciones, logging, depuración y gestión de memoria

**Fecha:** 03-08-2026  
**Bloque:** Bloque 1 — Fundamentos de Java moderno

## Propósito
Diseñar rutas de error observables, depurar sistemáticamente en VS Code y comprender memoria/GC sin realizar micro-optimizaciones prematuras.

## Resultados de aprendizaje

- Distinguir errores recuperables, errores de programación y fallos de infraestructura.
- Diseñar excepciones personalizadas, preservar causas y usar try-with-resources.
- Aplicar logging estructurado con SLF4J/Logback y contexto de correlación.
- Usar breakpoints, watches, call stack, conditional breakpoints y análisis de stack traces.
- Explicar heap, stack, alcance, elegibilidad para GC y fugas por referencias retenidas.

## Desarrollo de los temas

- Jerarquía Throwable; checked vs unchecked y política del curso.
- Try/catch/finally, multi-catch, try-with-resources y suppressed exceptions.
- Excepciones de dominio, infraestructura y traducción por capas.
- SLF4J/Logback, niveles, parámetros, MDC/correlationId y protección de datos sensibles.
- Depuración en VS Code: breakpoints condicionales, inspección, evaluación y excepción breakpoints.
- Heap, stack, metaspace, referencias y conceptos básicos de GC; por qué System.gc() no es garantía.
- Introducción a JFR/jcmd solo como observación, no tuning avanzado.

## Guion de presentación

- 1. Errores como parte del contrato, no como sorpresa.
- 2. Árbol de excepciones y decisiones checked/unchecked.
- 3. Demo: recurso AutoCloseable y suppressed exception.
- 4. Logging útil versus ruido; datos que nunca deben registrarse.
- 5. Demo VS Code: seguir una excepción desde endpoint simulado hasta causa raíz.
- 6. Modelo de memoria: stack, heap, referencias y GC.
- 7. Caso de fuga lógica por cache estática.
- 8. Checklist de diagnóstico reproducible.

## Secuencia temporal

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

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C03-E01 — Parser robusto | Manejo de errores, 15 min | Procesar CSV de solicitudes y reportar número de línea, campo y causa sin detener todo el lote. | **Entregable:** Parser y reporte de errores. **Criterios:** Entradas válidas continúan; errores preservan causa y contexto. |
| C03-E02 — Importador seguro | Try-with-resources, 15 min | Leer archivo con BufferedReader y recurso simulado que puede fallar al cerrar. | **Entregable:** Prueba de cierre y suppressed exceptions. **Criterios:** No hay cierre manual duplicado; recursos siempre cerrados. |
| C03-E03 — Trazabilidad de operación | Logging, 15 min | Agregar SLF4J con correlationId, niveles y mensajes parametrizados. | **Entregable:** logback.xml y salida de ejemplo. **Criterios:** No loguea contraseñas/tokens; evita concatenación costosa. |
| C03-E04 — Error intermitente por límite | Debugging, 25 min | Depurar cálculo que falla solo para prioridad máxima y fin de mes. | **Entregable:** debug-notes.md con hipótesis, breakpoint y causa. **Criterios:** Incluye evidencia de variables inspeccionadas y prueba regresiva. |
| C03-E05 — Transición inválida | Excepción de dominio, 20 min | Crear InvalidStateTransitionException y traducir errores de bajo nivel a lenguaje del dominio. | **Entregable:** Excepción, servicio y tests. **Criterios:** Causa original preservada cuando corresponde. |
| C03-E06 — Repositorio inestable | Fault injection, 25 min | Repositorio fake falla cada tercer llamado; servicio reacciona sin ocultar el fallo. | **Entregable:** Fake, pruebas y política documentada. **Criterios:** No implementa reintento infinito; logs incluyen intento y operación. |
| C03-E07 — Fuga por listener | Memoria, 18 min | Detectar objetos retenidos por listeners no removidos y corregir el ciclo de vida. | **Entregable:** Antes/después y explicación de referencias. **Criterios:** Prueba demuestra que colección no crece indefinidamente. |
| C03-E08 — Postmortem mínimo | Observabilidad, 18 min | A partir de logs desordenados, reconstruir una operación y proponer 5 mejoras de logging. | **Entregable:** postmortem.md. **Criterios:** Diferencia hechos, hipótesis y acción preventiva. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C03-T01 — Importador de lotes | 60-90 min | Importar solicitudes desde CSV con resumen de éxitos/fallos, errores de dominio y logging por lote. | **Entregable:** Aplicación y 15 pruebas. **Criterios:** Ningún catch vacío; recursos cerrados; logs sin PII innecesaria. |
| C03-T02 — Laboratorio de debugging | 60-90 min | Resolver cuatro bugs entregados y documentar para cada uno síntoma, hipótesis, evidencia, causa y prueba regresiva. | **Entregable:** docs/debug-lab.md y commits separados. **Criterios:** No se acepta solo “se corrigió”. |
| C03-T03 — Experimento de memoria | 60-90 min | Construir un programa que retenga referencias, observar crecimiento con herramienta del JDK y corregirlo. | **Entregable:** Informe con capturas y explicación. **Criterios:** No afirmar que GC libera objetos alcanzables; resultados reproducibles. |
| C03-T04 — Política de errores y logs | 60-90 min | Redactar estándar de 1–2 páginas para el proyecto integrador. | **Entregable:** docs/error-logging-policy.md. **Criterios:** Incluye niveles, correlación, datos prohibidos y ejemplos. |

## Evidencia de salida
Stack trace explicado, bug reproducido con depurador, prueba regresiva y logs legibles por operación.

## Preparación del docente
Preparar un repositorio con fallos inducidos, configuración de Logback y launch.json de VS Code.


---

# Clase 4: Collections, Streams, E/S, redes, concurrencia y pruebas básicas

**Fecha:** 13-08-2026  
**Bloque:** Bloque 1 — Fundamentos de Java moderno

## Propósito
Procesar datos de forma correcta y eficiente, consumir servicios HTTP, aplicar concurrencia moderna y dejar una base de pruebas/documentación viva.

## Resultados de aprendizaje

- Seleccionar List, Set, Map, Queue y colecciones inmutables según semántica.
- Usar Streams para filtrar, transformar, agrupar y reducir sin efectos secundarios ocultos.
- Aplicar NIO.2 y HttpClient para archivos y red.
- Distinguir concurrencia bloqueante, CompletableFuture y virtual threads.
- Escribir pruebas JUnit 5/AssertJ y JavaDoc/README que se mantengan con el código.

## Desarrollo de los temas

- Complejidad conceptual y contratos de List/Set/Map; orden, duplicados y mutabilidad.
- Comparator, generics, Optional y API Streams; cuándo preferir bucles.
- Collectors groupingBy, partitioningBy, mapping, reducing y manejo de errores.
- Files/Path, charset, streams de bytes/caracteres y serialización JSON conceptual.
- java.net.http.HttpClient, timeouts, status codes y parsing.
- Procesamiento multihilo: race conditions, ExecutorService, virtual threads y CompletableFuture.
- Límite Temporal futuro: no usar Thread/CompletableFuture/HTTP dentro de Workflow; trasladar a Activities.
- JUnit 5, AssertJ, pruebas parametrizadas y documentación viva.

## Guion de presentación

- 1. Elegir colección a partir de la regla de negocio.
- 2. Demo de igualdad y Set con objetos de dominio.
- 3. Pipeline Stream legible y versión imperativa equivalente.
- 4. Demo de Files y HttpClient con timeout.
- 5. Concurrencia: problema de carrera y solución.
- 6. Virtual threads: qué resuelven y qué no.
- 7. JUnit 5 parametrizado y documentación ejecutable.
- 8. Anticipo de restricciones de determinismo en Temporal.

## Secuencia temporal

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

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C04-E01 — Deduplicación de solicitudes | Collections, 15 min | Eliminar duplicados por identificador conservando el orden de llegada. | **Entregable:** Método y pruebas. **Criterios:** Semántica de igualdad explícita; orden preservado. |
| C04-E02 — Índice por responsable | Map, 15 min | Construir Map<Responsable,List<Solicitud>> y consultar sin NPE. | **Entregable:** Función y pruebas. **Criterios:** Colecciones devueltas no modificables o defensivas. |
| C04-E03 — Tablero de métricas | Streams, 20 min | Agrupar por estado y prioridad; calcular conteo y promedio de horas. | **Entregable:** DTO de métricas y pruebas. **Criterios:** Pipeline sin side effects; nombres intermedios legibles. |
| C04-E04 — Exportación atómica | NIO.2, 20 min | Escribir reporte a archivo temporal y moverlo al destino al finalizar. | **Entregable:** Exportador y prueba con directorio temporal. **Criterios:** Charset explícito; no deja archivo parcial ante fallo. |
| C04-E05 — Cliente de catálogo | HTTP, 25 min | Consumir endpoint stub con HttpClient, timeout y manejo de 2xx/4xx/5xx. | **Entregable:** Cliente tipado y tests con servidor stub. **Criterios:** No bloquea indefinidamente; error contiene status y URI segura. |
| C04-E06 — Procesamiento paralelo seguro | Concurrencia, 25 min | Procesar 100 solicitudes con virtual threads y acumular resultados sin carrera. | **Entregable:** Comparativa secuencial/concurrente. **Criterios:** Resultados deterministas; recursos cerrados; sin shared mutable state inseguro. |
| C04-E07 — Matriz parametrizada | Testing, 18 min | Pruebas parametrizadas para reglas de prioridad, estado y SLA. | **Entregable:** ParameterizedTest con al menos 12 casos. **Criterios:** Nombres de casos legibles y límites cubiertos. |
| C04-E08 — README ejecutable | Documentación viva, 18 min | Documentar cómo ejecutar, probar y reproducir un fallo; incluir ejemplo curl futuro. | **Entregable:** README y JavaDoc de API pública. **Criterios:** Comandos copiados funcionan; documentación coincide con código. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C04-T01 — Analizador de bitácoras | 60-90 min | Leer archivo grande, filtrar por correlationId, agrupar errores y generar reporte JSON. | **Entregable:** Módulo log-analyzer. **Criterios:** Streaming de archivo; no cargar todo innecesariamente; 20 pruebas. |
| C04-T02 — Agregador HTTP concurrente | 60-90 min | Consultar tres servicios stub en paralelo con timeout global y resultado parcial. | **Entregable:** Cliente y pruebas de fallos/latencia. **Criterios:** No pierde causa; limita concurrencia y documenta estrategia. |
| C04-T03 — Colecciones y rendimiento | 60-90 min | Comparar List/Set/Map en escenarios definidos y explicar resultados sin generalizaciones absolutas. | **Entregable:** Informe y benchmark simple o medición controlada. **Criterios:** Incluye warm-up y limitaciones del experimento. |
| C04-T04 — Contrato de pruebas | 60-90 min | Definir pirámide de pruebas y convención de nombres para el curso. | **Entregable:** docs/testing-strategy.md. **Criterios:** Incluye unidad, integración y futura prueba Temporal. |

## Evidencia de salida
Pipeline de datos probado, llamada HTTP con timeout y demostración de procesamiento concurrente sin carrera.

## Preparación del docente
Preparar datos CSV, servidor HTTP stub local, ejemplos de carrera y plantilla JUnit 5/AssertJ.


---

# Clase 5: Spring Boot 4: inyección de dependencias, REST y Thymeleaf

**Fecha:** 20-08-2026  
**Bloque:** Bloque 2 — Aplicaciones web y persistencia

## Propósito
Transformar el dominio en una aplicación web multicapa con API REST y una interfaz básica Thymeleaf, manteniendo separación de responsabilidades.

## Resultados de aprendizaje

- Crear proyecto Spring Boot 4 con Initializr y dependencias mínimas.
- Explicar IoC/DI y ciclo de vida de beans; usar inyección por constructor.
- Diseñar controladores REST, DTOs, servicio y repositorio en memoria.
- Aplicar validación de entrada y manejo uniforme de errores HTTP.
- Construir una vista Thymeleaf básica sin mezclar lógica de negocio.

## Desarrollo de los temas

- Spring Boot, auto-configuración, starters, perfiles y configuración externalizada.
- IoC/DI: beans, component scanning, @Configuration y constructor injection.
- Arquitectura por capas y vertical slices; DTO vs entidad de dominio.
- HTTP: métodos, códigos, headers, idempotencia básica y content negotiation.
- @RestController, @RequestMapping, @PathVariable, @RequestParam, @RequestBody.
- Bean Validation y Problem Details/handler global.
- MVC con Thymeleaf: controller, model, template y formularios.
- Actuator básico y health endpoint.

## Guion de presentación

- 1. De aplicación de consola a servicio web.
- 2. Contenedor Spring y diagrama de dependencias.
- 3. Initializr y pom mínimo; explicar cada starter.
- 4. Live coding CRUD en memoria por capas.
- 5. Contrato HTTP y errores consistentes.
- 6. Demo Thymeleaf con listado y formulario.
- 7. Prueba con MockMvc y curl.
- 8. Checklist de arquitectura y tarea.

## Secuencia temporal

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Revisión de arquitectura objetivo | Mostrar diagrama de capas del SIGEO. |
| 10–35 | IoC/DI y Spring Boot | Construir bean graph y detectar dependencias ocultas. |
| 35–60 | Demo Initializr + primer endpoint | Crear desde cero y ejecutar con perfil dev. |
| 60–80 | Ejercicios E01–E03 | Endpoints pequeños y DTOs. |
| 80–95 | Receso | Verificar que todos ejecuten la app. |
| 95–120 | Validación, errores y Thymeleaf | Mostrar handler global y formulario. |
| 120–160 | Laboratorio E04–E06 | CRUD en memoria y vista web. |
| 160–185 | Desafíos E07–E08 | Actuator y pruebas web. |
| 185–195 | Cierre y tarea | Revisión de contrato API y asignación. |

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C05-E01 — Health personalizado | Spring setup, 12 min | Crear proyecto y endpoint /api/health que reporte versión y estado sin exponer secretos. | **Entregable:** Proyecto y curl. **Criterios:** Arranca con ./mvnw spring-boot:run; respuesta JSON estable. |
| C05-E02 — Repositorio intercambiable | DI, 15 min | Definir interfaz y repositorio en memoria; inyectarlo por constructor en servicio. | **Entregable:** Beans y prueba de servicio. **Criterios:** No usar new en el servicio; dependencia visible. |
| C05-E03 — Crear solicitud | REST, 18 min | POST /api/solicitudes con DTO de entrada y 201 + Location. | **Entregable:** Controller, DTO y tests. **Criterios:** Valida obligatorios; no devuelve entidad interna directamente. |
| C05-E04 — Consulta y filtros | REST, 20 min | GET con filtros opcionales por estado/prioridad y 404 por id inexistente. | **Entregable:** Endpoints y pruebas MockMvc. **Criterios:** Códigos correctos; filtros componibles. |
| C05-E05 — Problem Details uniforme | Errores, 20 min | Implementar handler global para validación, not found y conflicto de estado. | **Entregable:** JSON de error y pruebas. **Criterios:** Incluye type/title/status/detail/instance o estructura equivalente. |
| C05-E06 — Panel web básico | Thymeleaf, 25 min | Listado, detalle y formulario de creación usando Thymeleaf. | **Entregable:** Templates y controlador MVC. **Criterios:** Escapa contenido; errores de validación visibles. |
| C05-E07 — Operabilidad mínima | Actuator, 15 min | Habilitar health/info y crear info de build sin exponer env completo. | **Entregable:** Configuración y evidencia. **Criterios:** Solo endpoints necesarios; health responde correctamente. |
| C05-E08 — Contrato MockMvc | Test web, 20 min | Agregar pruebas de 201, 400, 404 y conflicto 409. | **Entregable:** Suite MockMvc. **Criterios:** Aserciones sobre status, headers y cuerpo; no solo status. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C05-T01 — API SIGEO v1 | 60-90 min | Completar CRUD en memoria con DTOs, validación, errores y OpenAPI opcional. | **Entregable:** Aplicación y colección HTTP. **Criterios:** 25 pruebas; cobertura de transición inválida. |
| C05-T02 — Portal Thymeleaf | 60-90 min | Añadir edición, filtros y vista de errores amigable. | **Entregable:** Templates y capturas. **Criterios:** Sin lógica de negocio en HTML/controller. |
| C05-T03 — Prueba de arquitectura | 60-90 min | Crear pruebas que impidan que controller acceda directamente al repositorio. | **Entregable:** Test ArchUnit o verificación equivalente. **Criterios:** Falla ante dependencia prohibida. |
| C05-T04 — ADR de arquitectura | 60-90 min | Documentar decisión Spring Boot frente a despliegue WAR/Jakarta EE para este curso. | **Entregable:** docs/adr/0001-framework.md. **Criterios:** Contexto, opciones, decisión y consecuencias. |

## Evidencia de salida
API y vista web operativas, cuatro códigos HTTP demostrados y grafo de dependencias explicado.

## Preparación del docente
Preparar proyecto Initializr validado con Java 25/Spring Boot 4.1, colección HTTP y plantilla de Problem Details.


---

# Clase 6: Persistencia relacional con Spring Data JPA y PostgreSQL

**Fecha:** 27-08-2026  
**Bloque:** Bloque 2 — Aplicaciones web y persistencia

## Propósito
Persistir el modelo de forma segura, comprender ORM y evitar problemas habituales de mapeo, consultas y migraciones.

## Resultados de aprendizaje

- Configurar PostgreSQL con Docker Compose y perfiles de desarrollo/prueba.
- Mapear entidades, objetos valor y relaciones con JPA/Hibernate.
- Usar repositorios Spring Data, consultas derivadas, JPQL y projections.
- Aplicar migraciones de esquema y datos de prueba reproducibles.
- Detectar N+1, carga perezosa, cascadas peligrosas y exposición de entidades.

## Desarrollo de los temas

- JPA, Hibernate, EntityManager, persistence context y estados de entidad.
- @Entity, @Id, generación de IDs, @Version, embeddables y converters.
- Relaciones one-to-many/many-to-one; dueño de relación, fetch y cascade.
- Spring Data repositories, derived queries, @Query, projections y Specifications intro.
- PostgreSQL, H2 solo para pruebas rápidas y diferencias de dialecto.
- Migraciones con Flyway/Liquibase; no usar ddl-auto=create en producción.
- Transacciones como unidad de trabajo; detalle se profundiza en clase 7.
- Pruebas @DataJpaTest y Testcontainers recomendado.

## Guion de presentación

- 1. ORM: qué automatiza y qué no.
- 2. Ciclo de vida de entidad y persistence context.
- 3. Demo PostgreSQL + migración inicial.
- 4. Mapeo de Solicitud y Aprobación.
- 5. Repositorios y consultas derivadas.
- 6. N+1 y fetch join mediante evidencia SQL.
- 7. Testcontainers/@DataJpaTest.
- 8. Checklist de persistencia y seguridad de datos.

## Secuencia temporal

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Revisión del modelo relacional | Comparar diagrama de clases y tablas. |
| 10–35 | JPA y ciclo de vida | Mostrar persist, flush, clear y dirty checking. |
| 35–60 | Demo Docker Compose + Flyway | Levantar DB, migrar y consultar. |
| 60–80 | Ejercicios E01–E03 | Mapeos simples y repositorios. |
| 80–95 | Receso | Revisar conectividad y puertos. |
| 95–120 | Relaciones, consultas y N+1 | Activar SQL y analizar consultas. |
| 120–160 | Laboratorio E04–E06 | Consultas, paginación y prueba real. |
| 160–185 | Desafíos E07–E08 | Optimización y migración segura. |
| 185–195 | Cierre y tarea | Ticket: explicar owning side y riesgo de cascade. |

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C06-E01 — Solicitud persistente | Entidad, 15 min | Mapear entidad con UUID, estado, prioridad, timestamps y @Version. | **Entregable:** Entidad y migración. **Criterios:** Schema y mapping alineados; no usa entidad como DTO web. |
| C06-E02 — Datos de contacto | Embeddable, 15 min | Mapear objeto valor Contacto como @Embeddable con validación. | **Entregable:** Embeddable y test de persistencia. **Criterios:** Columnas claras; igualdad por valor. |
| C06-E03 — Consultas derivadas | Repositorio, 15 min | Crear consultas por estado, prioridad y rango de fecha. | **Entregable:** Repositorio y tests. **Criterios:** Nombres correctos; índices propuestos en migración. |
| C06-E04 — Historial de aprobación | Relaciones, 25 min | Mapear Solicitud 1:N Aprobacion evitando serialización recursiva. | **Entregable:** Entidades, servicio y tests. **Criterios:** Dueño de relación correcto; orphan removal justificado. |
| C06-E05 — Bandeja paginada | Paginación, 20 min | Endpoint paginado y ordenado, con límites de tamaño. | **Entregable:** Page DTO y pruebas. **Criterios:** No expone Page internamente sin contrato; máximo configurable. |
| C06-E06 — Resumen por responsable | JPQL/projection, 25 min | Consulta agregada que devuelve proyección con conteo y última fecha. | **Entregable:** Projection y test PostgreSQL. **Criterios:** Una consulta; tipos y nulls controlados. |
| C06-E07 — Caza N+1 | Diagnóstico, 18 min | Reproducir N+1, contar consultas y corregir con entity graph/fetch join. | **Entregable:** Informe antes/después. **Criterios:** Demuestra reducción con evidencia SQL. |
| C06-E08 — Cambio compatible | Migración, 18 min | Agregar campo obligatorio en dos pasos sin romper datos existentes. | **Entregable:** Migraciones V2/V3 y explicación. **Criterios:** Migración aplicable sobre base poblada; rollback lógico descrito. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C06-T01 — Persistencia completa SIGEO | 60-90 min | Reemplazar repositorio en memoria por PostgreSQL y migraciones versionadas. | **Entregable:** Aplicación, compose y migrations. **Criterios:** 30 pruebas, incluidos reinicio y datos persistentes. |
| C06-T02 — Catálogo de consultas | 60-90 min | Implementar 8 consultas: derivadas, JPQL, proyección, paginada y specification. | **Entregable:** Repositorio y matriz de pruebas. **Criterios:** Cada consulta documenta índice esperado. |
| C06-T03 — Pruebas con Testcontainers | 60-90 min | Ejecutar pruebas de repositorio contra PostgreSQL efímero. | **Entregable:** Perfil test y CI local. **Criterios:** No depende de DB instalada ni orden de pruebas. |
| C06-T04 — Revisión de mapeo | 60-90 min | Auditar cascadas, fetch y serialización; producir lista de riesgos y correcciones. | **Entregable:** docs/jpa-review.md. **Criterios:** Incluye al menos 6 hallazgos o justificaciones. |

## Evidencia de salida
Migraciones aplicadas desde cero, consultas verificadas contra PostgreSQL y un caso N+1 diagnosticado/corregido.

## Preparación del docente
Preparar Docker Compose de PostgreSQL, migraciones base, datos semilla y logging SQL controlado.


---

# Clase 7: Transacciones, concurrencia, depuración web, pruebas y documentación viva

**Fecha:** 03-09-2026  
**Bloque:** Bloque 2 — Aplicaciones web y persistencia

## Propósito
Hacer confiable la aplicación web mediante límites transaccionales claros, control de concurrencia, pruebas por capas y documentación ejecutable.

## Resultados de aprendizaje

- Definir límites de transacción en servicios y explicar propagación/rollback.
- Resolver lost update con bloqueo optimista y conocer casos de bloqueo pesimista.
- Diseñar pruebas unitarias, de slice e integración sin sobreusar mocks.
- Depurar una petición completa desde HTTP hasta SQL en VS Code.
- Mantener OpenAPI, ADR, diagramas y runbooks como documentación viva.

## Desarrollo de los temas

- ACID, autocommit y @Transactional: proxy, visibilidad, rollback y self-invocation.
- Optimistic locking con @Version; manejo de conflicto y reintento a nivel apropiado.
- Pessimistic locking, deadlock y timeout: uso excepcional.
- JUnit 5, Mockito, MockMvc, @DataJpaTest, @SpringBootTest y Testcontainers.
- Pruebas de contrato y arquitectura; test data builders.
- Debug web: breakpoint en controller/service/repository, SQL y correlationId.
- OpenAPI, JavaDoc, ADR, Mermaid y runbook de operación.
- Cobertura como señal, no objetivo aislado.

## Guion de presentación

- 1. Historia de dos transacciones que pisan datos.
- 2. @Transactional y el problema de proxy/self-invocation.
- 3. Demo lost update con dos clientes.
- 4. Solución con @Version y respuesta 409.
- 5. Pirámide de pruebas de la aplicación.
- 6. Demo Testcontainers y MockMvc.
- 7. Documentación viva y Definition of Done.
- 8. Code clinic de bugs integrados.

## Secuencia temporal

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Caso de carrera inicial | Simular dos aprobaciones concurrentes. |
| 10–35 | Transacciones y propagación | Dibujar límites y mostrar rollback. |
| 35–60 | Demo optimistic locking | Ejecutar dos requests y analizar excepción. |
| 60–80 | Ejercicios E01–E03 | Transacciones y conflicto. |
| 80–95 | Receso | Preparar suite de pruebas. |
| 95–120 | Estrategia de pruebas | Comparar test unitario, slice e integración. |
| 120–160 | Laboratorio E04–E06 | Debug y Testcontainers. |
| 160–185 | Desafíos E07–E08 | OpenAPI, ADR y arquitectura. |
| 185–195 | Cierre y tarea | Autoevaluación con Definition of Done. |

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C07-E01 — Rollback total | Transacción, 15 min | Servicio crea aprobación y actualiza solicitud; inducir fallo y verificar atomicidad. | **Entregable:** Test de integración. **Criterios:** Tras fallo, ninguna escritura parcial queda confirmada. |
| C07-E02 — Self-invocation | Diagnóstico, 15 min | Reproducir método @Transactional llamado internamente que no obtiene semántica esperada. | **Entregable:** Explicación y refactor. **Criterios:** Identifica proxy como causa; solución no depende de “magia”. |
| C07-E03 — Conflicto optimista | Concurrencia, 20 min | Dos actualizaciones con la misma versión; traducir conflicto a 409. | **Entregable:** Endpoint y test concurrente. **Criterios:** Una gana, otra recibe conflicto; no se pierden datos silenciosamente. |
| C07-E04 — Servicio de aprobación | Test unitario, 20 min | Probar reglas con fake/mock mínimo y test data builder. | **Entregable:** Suite unitaria. **Criterios:** No inicia Spring; casos de borde claros. |
| C07-E05 — Controller aislado | Slice test, 20 min | @WebMvcTest para contrato, validación y Problem Details. | **Entregable:** Pruebas web. **Criterios:** Servicio simulado; cuerpo y headers verificados. |
| C07-E06 — Flujo HTTP→DB | Integración, 25 min | @SpringBootTest + Testcontainers para crear, consultar y actualizar. | **Entregable:** Prueba de extremo de aplicación. **Criterios:** DB real; datos aislados; no depende de orden. |
| C07-E07 — Petición lenta | Debugging, 18 min | Seguir request con correlationId y hallar consulta inesperada. | **Entregable:** debug-trace.md. **Criterios:** Causa demostrada con stack/SQL; corrección medida. |
| C07-E08 — OpenAPI + ADR | Documentación, 18 min | Documentar endpoint de transición y decisión de optimistic locking. | **Entregable:** OpenAPI y ADR. **Criterios:** Ejemplos 200/409; consecuencias y alternativa evaluada. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C07-T01 — Suite por capas | 60-90 min | Construir 12 unit tests, 8 slice tests y 5 integration tests para SIGEO. | **Entregable:** Suite y matriz de cobertura funcional. **Criterios:** No duplicar el mismo caso en todas las capas sin propósito. |
| C07-T02 — Simulador de concurrencia | 60-90 min | Script que lance 20 actualizaciones concurrentes y reporte éxitos/conflictos. | **Entregable:** Script y análisis. **Criterios:** No hay lost updates; resultados repetibles. |
| C07-T03 — Runbook de fallos | 60-90 min | Documentar diagnóstico de app caída, DB no disponible, migración fallida y petición lenta. | **Entregable:** docs/runbook.md. **Criterios:** Comandos concretos, señales esperadas y escalamiento. |
| C07-T04 — Documentación viva | 60-90 min | Actualizar diagrama C4/mermaid, OpenAPI y ADR con cambios reales. | **Entregable:** docs actualizados. **Criterios:** Cada artefacto enlaza al código relevante. |

## Evidencia de salida
Prueba de rollback, demostración de conflicto 409 y suite de integración ejecutada en entorno limpio.

## Preparación del docente
Preparar escenario de lost update, Testcontainers, colección concurrente y plantilla de ADR/OpenAPI.


---

# Clase 8: Seguridad web y empresarial con Spring Security

**Fecha:** 10-09-2026  
**Bloque:** Bloque 2 — Aplicaciones web y persistencia

## Propósito
Aplicar defensa en profundidad: autenticación, autorización, validación, gestión de secretos y pruebas de seguridad.

## Resultados de aprendizaje

- Explicar amenazas principales de una API y el principio de mínimo privilegio.
- Configurar Spring Security con SecurityFilterChain.
- Implementar autenticación con usuarios de prueba y JWT/OAuth2 Resource Server según alcance.
- Aplicar roles y permisos a endpoints y métodos.
- Probar 401/403, CSRF según arquitectura, CORS, headers y no exposición de secretos.

## Desarrollo de los temas

- Autenticación vs autorización; identidad, rol, permiso y contexto de seguridad.
- SecurityFilterChain, PasswordEncoder y gestión de usuarios.
- Sesión web/CSRF frente a API token/JWT; decisión arquitectónica.
- OAuth2 Resource Server conceptual; validación de issuer/audience/exp.
- @PreAuthorize, method security y ownership checks.
- CORS, headers, rate limiting conceptual y validación de entrada.
- Secretos en variables/secret manager; nunca en Git ni logs.
- Security tests, auditoría y checklist OWASP aplicado.

## Guion de presentación

- 1. Modelo de amenaza de SIGEO.
- 2. Flujo de filtros de Spring Security.
- 3. Demo seguridad por defecto y configuración explícita.
- 4. JWT: estructura, firma y claims; no confundir con cifrado.
- 5. Autorización por rol y por propietario.
- 6. CSRF/CORS con escenarios.
- 7. Pruebas de seguridad y gestión de secretos.
- 8. Mini auditoría y tarea.

## Secuencia temporal

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Threat modeling rápido | Identificar activos, actores y abusos. |
| 10–35 | Fundamentos y filtro de seguridad | Dibujar autenticación y autorización. |
| 35–60 | Demo SecurityFilterChain | Proteger endpoints y generar usuarios de laboratorio. |
| 60–80 | Ejercicios E01–E03 | 401/403, password y roles. |
| 80–95 | Receso | Preparar tokens de prueba. |
| 95–120 | JWT, CSRF, CORS y ownership | Mostrar ataques simples controlados. |
| 120–160 | Laboratorio E04–E06 | Securizar API y vistas. |
| 160–185 | Desafíos E07–E08 | Auditoría y secretos. |
| 185–195 | Cierre y tarea | Entregar threat model actualizado. |

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C08-E01 — Denegar por defecto | Configuración, 15 min | Configurar rutas públicas mínimas y proteger el resto. | **Entregable:** SecurityFilterChain y pruebas. **Criterios:** Endpoint no declarado queda protegido; no usar permitAll global. |
| C08-E02 — Usuarios de laboratorio | Contraseñas, 15 min | Crear usuarios con BCrypt y roles distintos sin contraseñas en código final. | **Entregable:** Config dev y test. **Criterios:** Passwords codificados; secretos externalizados. |
| C08-E03 — Roles por operación | Autorización, 18 min | LECTOR consulta, OPERADOR crea y SUPERVISOR aprueba. | **Entregable:** Reglas y tests 200/401/403. **Criterios:** Matriz de permisos completa y consistente. |
| C08-E04 — Resource server local | JWT, 25 min | Validar JWT de laboratorio, issuer/audience y expiración. | **Entregable:** Configuración y pruebas. **Criterios:** Rechaza firma, audience o exp inválidos. |
| C08-E05 — Editar solo lo propio | Ownership, 20 min | Además del rol, verificar que solicitante edite su solicitud salvo supervisor. | **Entregable:** Policy/service y tests. **Criterios:** No confiar solo en id enviado por cliente. |
| C08-E06 — CSRF y formulario | Web security, 20 min | Proteger formulario Thymeleaf con CSRF y demostrar fallo sin token. | **Entregable:** Template y test. **Criterios:** Token presente; sesión y logout correctos. |
| C08-E07 — Escáner de secretos | Secrets, 15 min | Eliminar clave accidental del repo, rotar valor simulado y agregar prevención. | **Entregable:** Informe y pre-commit/CI local. **Criterios:** El secreto no permanece en archivos actuales; plan considera historial. |
| C08-E08 — Abuse cases | Auditoría, 20 min | Ejecutar checklist de 12 pruebas: IDOR, mass assignment, errores verbosos, CORS y logs. | **Entregable:** security-review.md. **Criterios:** Cada hallazgo tiene severidad, evidencia y corrección. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C08-T01 — SIGEO seguro | 60-90 min | Aplicar JWT/roles/ownership a toda la API y sesión segura a Thymeleaf. | **Entregable:** Aplicación y 25 security tests. **Criterios:** Matriz de acceso demostrable; secretos fuera del repo. |
| C08-T02 — Threat model formal | 60-90 min | Crear DFD simple, trust boundaries y 10 amenazas STRIDE con mitigaciones. | **Entregable:** docs/threat-model.md. **Criterios:** Amenazas vinculadas a componentes reales. |
| C08-T03 — Prueba negativa | 60-90 min | Construir colección de requests maliciosas y resultados esperados. | **Entregable:** security-tests.http o Postman. **Criterios:** Incluye 401,403,404 anti-enumeración, 400 y rate-limit conceptual. |
| C08-T04 — Política de datos | 60-90 min | Clasificar datos del sistema y definir qué puede ir en logs, payloads y backups. | **Entregable:** docs/data-classification.md. **Criterios:** Incluye retención y minimización. |

## Evidencia de salida
Matriz de permisos validada por pruebas y demostración de token inválido, usuario sin rol y acceso autorizado.

## Preparación del docente
Preparar issuer/JWK local o tokens firmados de laboratorio, usuarios de prueba y colección de ataques controlados.


---

# Clase 9: Temporal.io: arquitectura y ejecución duradera

**Fecha:** 17-09-2026  
**Bloque:** Bloque 3 — Workflows resilientes y sistemas distribuidos

## Propósito
Comprender el modelo mental de Temporal y construir el primer Workflow Java determinista con Worker, Activity y Client.

## Resultados de aprendizaje

- Explicar por qué cron, colas y estados manuales no bastan para procesos largos.
- Identificar Temporal Service, Namespace, Task Queue, Worker, Workflow, Activity, Client y Event History.
- Crear y ejecutar un Workflow con interfaz e implementación Java.
- Observar replay, reinicio del Worker y continuidad del proceso.
- Aplicar reglas de determinismo desde el primer ejercicio.

## Desarrollo de los temas

- Ejecución duradera, event sourcing interno y Event History.
- Temporal Service vs Worker: el servidor no ejecuta código de negocio.
- Workflow Definition/Execution, Workflow ID, Run ID y Task Queue.
- Activities para efectos externos y Workflows para orquestación.
- Determinismo y replay: no I/O, reloj del sistema, UUID aleatorio ni threads nativos en Workflow.
- APIs Temporal: Workflow.currentTimeMillis, Workflow.sleep, Async/Promise.
- Temporal CLI dev server, UI y comandos básicos.
- Modelado de un proceso de aprobación duradero.

## Guion de presentación

- 1. Fallos clásicos de procesos distribuidos.
- 2. Arquitectura Temporal con separación Service/Worker.
- 3. Event History y replay narrados paso a paso.
- 4. Reglas de determinismo con ejemplos correctos/incorrectos.
- 5. Demo CLI dev server y UI.
- 6. Live coding Workflow/Activity/Worker/Client.
- 7. Matar/reiniciar Worker sin perder progreso.
- 8. Checklist Temporal del curso.

## Secuencia temporal

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

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C09-E01 — Temporal local | Infraestructura, 12 min | Iniciar Temporal CLI dev server, abrir UI y describir namespace/task queue. | **Entregable:** Comandos y captura. **Criterios:** Servidor accesible; no usar Docker Compose legado si CLI está disponible. |
| C09-E02 — Saludo duradero | Hello Workflow, 18 min | Implementar interfaz @WorkflowInterface y método @WorkflowMethod. | **Entregable:** Workflow, Worker y starter. **Criterios:** Workflow ID explícito; resultado visible en UI. |
| C09-E03 — Registrar auditoría | Activity, 18 min | Mover escritura simulada de auditoría a @ActivityInterface. | **Entregable:** Activity y opciones. **Criterios:** No hace I/O desde Workflow; Activity registrada en Worker. |
| C09-E04 — Espera de revisión | Timer durable, 20 min | Usar Workflow.sleep para simular plazo y observar Timer events. | **Entregable:** Workflow y captura de history. **Criterios:** No Thread.sleep; replay correcto. |
| C09-E05 — Reinicio controlado | Durabilidad, 25 min | Iniciar Workflow, detener Worker, esperar y reiniciar. | **Entregable:** Informe de observación. **Criterios:** Mismo Workflow continúa; diferencia Workflow ID/Run ID explicada. |
| C09-E06 — Aprobación v0 | Modelado, 25 min | Workflow que registra solicitud, espera plazo y marca vencida si no hay decisión simulada. | **Entregable:** Workflow y tests básicos. **Criterios:** Estado solo en Workflow; Activity para persistencia/notificación. |
| C09-E07 — Detectar no determinismo | Revisión, 18 min | Encontrar 10 usos prohibidos en un Workflow: UUID, Instant.now, HTTP, DB, Thread, etc. | **Entregable:** review.md y correcciones. **Criterios:** Cada corrección usa API Temporal o Activity adecuada. |
| C09-E08 — Leer la historia | Event History, 18 min | Etiquetar eventos de un run y relacionarlos con líneas del código. | **Entregable:** history-walkthrough.md. **Criterios:** Distingue Workflow Task, Activity Task y Timer. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C09-T01 — Workflow de expediente | 60-90 min | Orquestar creación, validación y notificación simulada con 3 Activities. | **Entregable:** Módulo temporal-c09. **Criterios:** Determinismo revisado; Workflow ID de negocio; 8 pruebas. |
| C09-T02 — Guía de reglas Temporal | 60-90 min | Crear checklist de código permitido/prohibido dentro de Workflow con ejemplos Java. | **Entregable:** docs/temporal-determinism.md. **Criterios:** Incluye reloj, aleatoriedad, I/O, threads, config y versionado. |
| C09-T03 — Análisis de historia | 60-90 min | Exportar una Event History y explicar 15 eventos relevantes. | **Entregable:** docs/history-analysis.md. **Criterios:** Relaciona comandos con eventos y reintentos. |
| C09-T04 — Integración Spring inicial | 60-90 min | Crear aplicación Spring Boot que inyecte WorkflowClient e inicie Workflow desde endpoint. | **Entregable:** Proyecto y prueba web. **Criterios:** Controller no contiene lógica de orquestación; configuración externalizada. |

## Evidencia de salida
Workflow continúa después de reiniciar Worker y el alumno justifica cada operación ubicada en Workflow o Activity.

## Preparación del docente
Instalar Temporal CLI, preparar módulo Maven con temporal-sdk 1.37.0 y verificar compatibilidad con Java 25.


---

# Clase 10: Activities, timeouts, reintentos, heartbeats e idempotencia

**Fecha:** 24-09-2026  
**Bloque:** Bloque 3 — Workflows resilientes y sistemas distribuidos

## Propósito
Diseñar Activities robustas frente a fallos transitorios, permanentes, duplicados y operaciones largas.

## Resultados de aprendizaje

- Configurar Start-to-Close, Schedule-to-Close, Schedule-to-Start y Heartbeat timeouts según el caso.
- Diseñar RetryOptions y clasificar errores no reintentables.
- Hacer Activities idempotentes usando claves de negocio y registros de deduplicación.
- Emitir heartbeats y reanudar progreso de actividades largas.
- Aplicar cancelación y compensación sin reintentos infinitos.

## Desarrollo de los temas

- Semántica at-least-once de Activities y consecuencias.
- ActivityOptions y tipos de timeout; evitar valores ausentes.
- RetryPolicy: backoff, maximum attempts, maximum interval y non-retryable failures.
- ApplicationFailure y clasificación de fallos.
- Idempotency key basada en Workflow ID/Activity ID o negocio.
- Heartbeats, details de progreso y cancelación cooperativa.
- Activity context, logging y métricas por intento.
- Fault injection y pruebas deterministas.

## Guion de presentación

- 1. Por qué una Activity puede ejecutarse más de una vez.
- 2. Matriz de timeouts y ejemplos.
- 3. Demo servicio externo inestable con RetryOptions.
- 4. Idempotencia: antes/después con cargo duplicado.
- 5. Heartbeat de procesamiento por lotes.
- 6. Errores no reintentables y cancelación.
- 7. Pruebas con fallos programados.
- 8. Checklist de Activity de producción.

## Secuencia temporal

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Quiz de timeouts | Elegir timeout para 5 Activities. |
| 10–35 | Semántica y reintentos | Dibujar intentos y backoff. |
| 35–60 | Demo Activity inestable | Observar intentos en UI. |
| 60–80 | Ejercicios E01–E03 | Timeouts, retry y error classification. |
| 80–95 | Receso | Preparar Activity larga. |
| 95–120 | Idempotencia y heartbeats | Mostrar deduplicación y resume. |
| 120–160 | Laboratorio E04–E06 | Procesamiento de lote y cancelación. |
| 160–185 | Desafíos E07–E08 | Métricas y fault matrix. |
| 185–195 | Cierre y tarea | Revisión de política de fallos. |

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C10-E01 — Actividad HTTP acotada | Timeouts, 15 min | Configurar timeouts para llamada externa de 2 s y simular latencias 1/3/10 s. | **Entregable:** ActivityOptions y tabla de resultados. **Criterios:** Falla dentro de tiempo previsto; no depende de timeout infinito. |
| C10-E02 — Servicio 503 temporal | Retry, 18 min | Reintentar 503 con backoff y detener ante 400. | **Entregable:** RetryOptions y tests. **Criterios:** 400 clasificado no reintentable; máximo de intentos explícito. |
| C10-E03 — ApplicationFailure tipada | Error model, 15 min | Emitir códigos VALIDATION, NOT_FOUND y PROVIDER_UNAVAILABLE. | **Entregable:** Activity y manejo en Workflow. **Criterios:** Workflow decide según tipo, no parsea mensajes. |
| C10-E04 — Reserva única | Idempotencia, 25 min | Activity de reserva acepta idempotency key y evita duplicados al repetirse. | **Entregable:** Repositorio fake y test de doble invocación. **Criterios:** Mismo comando retorna mismo resultado sin segunda reserva. |
| C10-E05 — Procesamiento por páginas | Heartbeat, 25 min | Procesar 1000 registros por páginas, heartbeat de último offset y reanudar. | **Entregable:** Activity y prueba de interrupción. **Criterios:** No reprocesa más de la ventana permitida; progreso visible. |
| C10-E06 — Cancelar exportación | Cancelación, 20 min | Detectar cancelación durante Activity larga y cerrar recursos. | **Entregable:** Workflow/Activity y test. **Criterios:** Cancelación cooperativa; cleanup idempotente. |
| C10-E07 — Intento y latencia | Observabilidad, 18 min | Agregar logs/metrics con workflowId, activityId e intento sin duplicar datos sensibles. | **Entregable:** Salida y panel textual. **Criterios:** Permite distinguir intento y causa; no imprime payload completo. |
| C10-E08 — Tabla de resiliencia | Fault matrix, 18 min | Ejecutar 8 combinaciones de fallo/timeout/retry y documentar resultado esperado/real. | **Entregable:** resilience-matrix.md. **Criterios:** Coincidencia razonada; anomalías investigadas. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C10-T01 — Integración inestable | 60-90 min | Workflow llama a catálogo y notificación simulados con fallos programables, timeouts y retry por tipo. | **Entregable:** Módulo y 15 pruebas. **Criterios:** Sin retry infinito; errores permanentes terminan rápido. |
| C10-T02 — Actividad idempotente real | 60-90 min | Persistir deduplicación en PostgreSQL con clave única y manejar concurrencia. | **Entregable:** Activity y migración. **Criterios:** Dos ejecuciones concurrentes producen un solo efecto. |
| C10-T03 — Activity larga reanudable | 60-90 min | Importar archivo grande con heartbeat de offset y cancelación. | **Entregable:** Implementación y prueba de reinicio. **Criterios:** Reanuda desde progreso; cierre seguro de archivo. |
| C10-T04 — Runbook de Activity fallida | 60-90 min | Procedimiento para inspeccionar, resetear/reintentar o corregir un fallo sin manipular DB a ciegas. | **Entregable:** docs/activity-runbook.md. **Criterios:** Incluye criterios para fallo transitorio/permanente. |

## Evidencia de salida
Matriz de fallos ejecutada, evidencia de reintentos en UI y demostración de idempotencia ante doble ejecución.

## Preparación del docente
Preparar servicios stub con latencia/status configurables y proyecto con temporal-testing.


---

# Clase 11: Interacción con Workflows: Signals, Queries, Updates, timers y Continue-As-New

**Fecha:** 01-10-2026  
**Bloque:** Bloque 3 — Workflows resilientes y sistemas distribuidos

## Propósito
Construir Workflows de larga duración que reciban decisiones externas, expongan estado y controlen el crecimiento de su historia.

## Resultados de aprendizaje

- Usar Signals para eventos asíncronos, Queries para lectura y Updates para operaciones confirmadas.
- Validar mensajes y evitar condiciones de carrera lógicas en el Workflow.
- Esperar condiciones con Workflow.await y usar timers durables.
- Orquestar Child Workflows y manejar su política de cierre.
- Aplicar Continue-As-New para limitar Event History y preservar estado esencial.

## Desarrollo de los temas

- Message passing: Signal, Query y Update; diferencias semánticas y casos de uso.
- @SignalMethod, @QueryMethod, @UpdateMethod y validadores de Update.
- Workflow.await, timers, selectores y estado interno.
- Deduplicación de mensajes y correlación por commandId.
- Child Workflows, parent close policy y cancelación.
- Continue-As-New, límites de history y carry-over de estado.
- Workflow ID reuse/conflict policies conceptual.
- Pruebas de interacción y time skipping.

## Guion de presentación

- 1. Tabla comparativa Signal/Query/Update.
- 2. Proceso de aprobación interactivo.
- 3. Demo Workflow.await y timer de expiración.
- 4. Update con validación y resultado síncrono.
- 5. Child Workflow por etapa.
- 6. Continue-As-New y tamaño de historia.
- 7. Pruebas con time skipping.
- 8. Antipatrones de message passing.

## Secuencia temporal

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

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C11-E01 — Aprobar o rechazar | Signal, 18 min | Agregar señales approve/reject a Workflow en espera. | **Entregable:** Workflow y cliente. **Criterios:** Ignora transición inválida de forma definida; resultado durable. |
| C11-E02 — Estado consultable | Query, 15 min | Exponer estado, historial resumido y deadline sin modificar Workflow. | **Entregable:** Queries y tests. **Criterios:** Query no hace I/O ni muta estado. |
| C11-E03 — Vencimiento automático | Await/timer, 20 min | Esperar decisión o timeout, lo que ocurra primero. | **Entregable:** Workflow y test time-skipping. **Criterios:** No Thread.sleep; resultado correcto en ambos caminos. |
| C11-E04 — Cambiar prioridad confirmado | Update, 20 min | Update valida estado y retorna nueva prioridad. | **Entregable:** Update + validator y tests. **Criterios:** Entrada inválida rechazada antes de handler; respuesta confirmada. |
| C11-E05 — Comando repetido | Deduplicación, 20 min | Incluir commandId y evitar procesar dos veces misma aprobación. | **Entregable:** Estado de dedupe y test. **Criterios:** Reintento del cliente no duplica transición. |
| C11-E06 — Revisión especializada | Child Workflow, 25 min | Delegar evaluación técnica a child workflow con timeout/cancelación. | **Entregable:** Parent/child y tests. **Criterios:** Parent close policy explícita; errores propagados o manejados. |
| C11-E07 — Bandeja de eventos larga | Continue-As-New, 20 min | Tras N eventos, continuar como nuevo conservando estado compacto. | **Entregable:** Workflow y análisis de runs. **Criterios:** History se reinicia; estado esencial preservado. |
| C11-E08 — Aprobación vs expiración | Race logic, 18 min | Simular señal cercana al timer y definir política determinista. | **Entregable:** Pruebas repetidas y decisión documentada. **Criterios:** Resultado consistente con regla explícita. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C11-T01 — Aprobación multinivel | 60-90 min | Workflow con dos niveles, Signals/Updates, Queries y vencimientos por etapa. | **Entregable:** Módulo y 20 pruebas. **Criterios:** Interacciones idempotentes; estados exhaustivos. |
| C11-T02 — Cliente operativo | 60-90 min | CLI o endpoints Spring para iniciar, consultar, actualizar, señalar y cancelar Workflows. | **Entregable:** Cliente y colección HTTP. **Criterios:** Errores de workflow no se traducen a 500 genérico. |
| C11-T03 — Continue-As-New controlado | 60-90 min | Procesar 500 eventos simulados y continuar cada 50; registrar runs. | **Entregable:** Informe y tests. **Criterios:** No acumula estado innecesario; búsquedas siguen siendo posibles. |
| C11-T04 — Contrato de mensajes | 60-90 min | Definir versionado, commandId, validación y compatibilidad de Signals/Updates. | **Entregable:** docs/workflow-messages.md. **Criterios:** Incluye estrategia para clientes antiguos. |

## Evidencia de salida
Workflow interactivo probado con señal, query, update, timeout y Continue-As-New.

## Preparación del docente
Preparar cliente de interacción, escenarios de carrera y pruebas con time skipping.


---

# Clase 12: Microservicios, transacciones distribuidas y patrón Saga

**Fecha:** 08-10-2026  
**Bloque:** Bloque 3 — Workflows resilientes y sistemas distribuidos

## Propósito
Orquestar una operación distribuida con compensaciones explícitas, idempotencia y visibilidad de estado.

## Resultados de aprendizaje

- Explicar por qué una transacción ACID no cruza microservicios de forma práctica.
- Distinguir saga orquestada y coreografiada, y seleccionar Temporal para orquestación.
- Diseñar pasos, compensaciones y orden inverso de rollback.
- Manejar fallos de compensación y estados parcialmente compensados.
- Integrar Spring Boot, PostgreSQL y Temporal sin mezclar transacciones locales con Workflow state.

## Desarrollo de los temas

- Consistencia fuerte/eventual, 2PC conceptual y límites operativos.
- Saga: forward recovery, backward recovery y compensaciones semánticas.
- Orden de registro de compensación antes/después del efecto.
- Idempotencia de pasos y compensaciones.
- Manejo de fallos de compensación y operación manual asistida.
- Child Workflows y separación por bounded context.
- Outbox/inbox como complemento para publicación de eventos.
- Caso integrador: reservar recurso, presupuesto, agenda y notificar.

## Guion de presentación

- 1. Fallo en el paso 3 de 4: estado imposible.
- 2. 2PC vs Saga sin simplificaciones engañosas.
- 3. Diseño de compensaciones de negocio.
- 4. Demo Saga con pila de compensaciones.
- 5. Fallo durante compensación.
- 6. Persistencia local y Workflow como coordinador.
- 7. Outbox conceptual y mensajería futura.
- 8. Revisión de arquitectura distribuida.

## Secuencia temporal

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Juego de estados parciales | Analizar qué revertir en cinco fallos. |
| 10–35 | Transacciones distribuidas | Comparar 2PC, saga y coreografía. |
| 35–60 | Demo Saga Temporal | Ejecutar flujo con fallo inducido. |
| 60–80 | Ejercicios E01–E03 | Pasos y compensaciones. |
| 80–95 | Receso | Preparar servicios stub. |
| 95–120 | Fallos de compensación e idempotencia | Mostrar estado “requiere intervención”. |
| 120–160 | Laboratorio E04–E06 | Saga completa. |
| 160–185 | Desafíos E07–E08 | Outbox y child workflow. |
| 185–195 | Cierre y tarea | Defensa de diseño de compensación. |

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C12-E01 — Mapa de pasos | Modelado Saga, 15 min | Definir pasos reserva-presupuesto-agenda-notificación y compensación de cada uno. | **Entregable:** Tabla de saga. **Criterios:** Compensaciones son semánticas, no “rollback SQL remoto”. |
| C12-E02 — Saga mínima | Implementación, 20 min | Implementar dos pasos y compensar el primero si falla el segundo. | **Entregable:** Workflow y tests. **Criterios:** Compensación registrada en orden seguro e idempotente. |
| C12-E03 — Fallo por etapa | Fault injection, 20 min | Parametrizar fallo en cada paso y verificar estado final. | **Entregable:** Pruebas parametrizadas. **Criterios:** Todos los recursos quedan liberados o marcados para intervención. |
| C12-E04 — Compensación inestable | Compensation failure, 25 min | Hacer fallar liberación temporalmente y configurar retry diferente. | **Entregable:** Workflow y matriz. **Criterios:** No pierde necesidad de compensar; visibilidad del fallo. |
| C12-E05 — Doble cancelación | Idempotencia, 20 min | Compensar dos veces sin error ni efecto duplicado. | **Entregable:** Activity y pruebas. **Criterios:** Resultado estable ante repetición y concurrencia. |
| C12-E06 — Endpoint de operación | Integración Spring, 25 min | POST inicia saga y GET consulta estado/resultado. | **Entregable:** Spring + WorkflowClient. **Criterios:** HTTP no espera indefinidamente; IDs correlacionados. |
| C12-E07 — Contextos separados | Child workflows, 20 min | Convertir reserva de recurso y presupuesto en child workflows. | **Entregable:** Arquitectura y código. **Criterios:** Task queues y ownership definidos. |
| C12-E08 — Evento de completitud | Outbox conceptual, 18 min | Diseñar tabla outbox y publicador idempotente al finalizar saga. | **Entregable:** Migración/pseudocódigo o implementación. **Criterios:** Evita commit DB + publish no atómico; clave de dedupe definida. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C12-T01 — Saga de asignación | 60-90 min | Implementar saga de 4 pasos con 4 fallos inducibles y compensaciones. | **Entregable:** Módulo + 25 pruebas. **Criterios:** Estados finales documentados; no hay efectos duplicados. |
| C12-T02 — Panel de seguimiento | 60-90 min | Exponer estado de saga, pasos completados y compensaciones desde Query/DB. | **Entregable:** API y vista simple. **Criterios:** No usa Query para I/O; combina fuentes en capa de consulta. |
| C12-T03 — Chaos script | 60-90 min | Ejecutar 50 sagas con probabilidades de fallo y resumir resultados. | **Entregable:** Script e informe. **Criterios:** Cero invariantes rotas; fallos pendientes identificables. |
| C12-T04 — ADR de consistencia | 60-90 min | Comparar saga orquestada, coreografía y 2PC para el caso. | **Entregable:** ADR. **Criterios:** Incluye operación, observabilidad y recuperación. |

## Evidencia de salida
Saga demostrada con fallo en cada etapa, compensación visible y invariantes finales verificadas.

## Preparación del docente
Preparar cuatro servicios stub, esquema de estados y framework de fault injection.


---

# Clase 13: Pruebas Temporal, replay, versionado, observabilidad y seguridad

**Fecha:** 15-10-2026  
**Bloque:** Bloque 3 — Workflows resilientes y sistemas distribuidos

## Propósito
Preparar Workflows para evolución segura, operación observable y conexión protegida.

## Resultados de aprendizaje

- Escribir pruebas de integración Temporal con TestWorkflowEnvironment y salto de tiempo.
- Aplicar replay testing contra historias reales antes de desplegar cambios.
- Usar versionado/patching y Continue-As-New para cambios compatibles.
- Instrumentar métricas, tracing, logs y Search Attributes sin filtrar datos sensibles.
- Configurar acceso local/cloud con API key o mTLS y separar secretos/payloads.

## Desarrollo de los temas

- Estrategia de pruebas Temporal: mayoría integración, Activities simuladas según objetivo.
- TestWorkflowEnvironment, time skipping y pruebas de señales/updates/cancelación.
- Replay de Event History y detección de nondeterminism.
- Workflow.getVersion/patching y safe deployment; versiones de Worker conceptuales.
- Search Attributes, Memo y visibilidad; datos indexados vs payload.
- Metrics/Actuator/Micrometer, OpenTelemetry y correlación.
- Seguridad: namespace, API key/mTLS, least privilege, secretos y rotación.
- Payload codecs/encryption conceptual; minimización y retención de History.

## Guion de presentación

- 1. Qué probar en Workflow vs Activity.
- 2. Time skipping: workflow de 30 días en segundos.
- 3. Replay test y cambio no determinista.
- 4. Versionado de Workflow con rama antigua/nueva.
- 5. Search Attributes y consulta operativa.
- 6. Métricas/traces/logs y replay-safe logging.
- 7. Seguridad de conexión y datos en History.
- 8. Gate de despliegue seguro.

## Secuencia temporal

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

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C13-E01 — Vencimiento en segundos | Temporal test, 20 min | Probar Workflow con timer de 30 días usando time skipping. | **Entregable:** TestWorkflowEnvironment. **Criterios:** Test dura segundos; código de producción sin reloj inyectado artificial. |
| C13-E02 — Signals y Updates | Interaction test, 20 min | Probar señal, query, update inválido y cancelación. | **Entregable:** Suite integración. **Criterios:** Orden de interacción controlado; resultados exactos. |
| C13-E03 — Historia incompatible | Replay, 20 min | Ejecutar replay de historia y detectar cambio de orden de Activities. | **Entregable:** Test y explicación. **Criterios:** Falla antes del fix; pasa tras estrategia compatible. |
| C13-E04 — Nueva validación | Versioning, 25 min | Introducir rama versionada para nuevos Workflows preservando los antiguos. | **Entregable:** Código y dos historias de prueba. **Criterios:** Ambas versiones replayan; plan para retirar código antiguo. |
| C13-E05 — Search Attributes operativos | Visibility, 18 min | Indexar estado, prioridad y responsable; consultar workflows. | **Entregable:** Configuración y comandos. **Criterios:** No coloca PII sensible; tipos de atributo correctos. |
| C13-E06 — Correlación end-to-end | Observability, 20 min | Propagar correlationId y observar HTTP→Workflow→Activity. | **Entregable:** Logs/traces y captura. **Criterios:** No depende de logs duplicados en replay; IDs consistentes. |
| C13-E07 — Configuración sin secretos | Security, 18 min | Externalizar target, namespace y API key/mTLS; perfiles local/cloud. | **Entregable:** application.yml template y .env.example. **Criterios:** Ninguna clave real; TLS/API key configurables. |
| C13-E08 — History hygiene | Data audit, 18 min | Revisar payloads, errores y Search Attributes para detectar datos excesivos. | **Entregable:** audit.md. **Criterios:** Propone redacción, referencia por ID o codec cuando corresponde. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C13-T01 — Suite Temporal completa | 60-90 min | Agregar 20 pruebas con time skipping, fault injection, signals, updates, cancellation y replay. | **Entregable:** Suite CI. **Criterios:** Mayoría integración; version de temporal-testing alineada con SDK. |
| C13-T02 — Gate de replay | 60-90 min | Crear comando CI que descargue/use historias de fixtures y ejecute replay. | **Entregable:** Script y histories anonimizadas. **Criterios:** Falla ante nondeterminism y documenta actualización de fixtures. |
| C13-T03 — Dashboard operativo | 60-90 min | Definir métricas, SLO y consultas de visibilidad para workflows críticos. | **Entregable:** docs/observability.md. **Criterios:** Incluye latencia, fallos, retries, task queue y pendientes. |
| C13-T04 — Modelo de seguridad Temporal | 60-90 min | Threat model de conexión, workers, payloads, secretos y acceso a UI/namespace. | **Entregable:** docs/temporal-security.md. **Criterios:** Mitigaciones priorizadas y responsabilidades claras. |

## Evidencia de salida
Suite con time skipping y replay ejecutada, Search Attributes consultables y configuración segura sin secretos.

## Preparación del docente
Preparar historias de Workflow anonimizadas, métricas de ejemplo y perfiles local/cloud ficticios.


---

# Clase 14: Inteligencia artificial en aplicaciones Java y agentes durables

**Fecha:** 22-10-2026  
**Bloque:** Bloque 4 — IA y tecnologías avanzadas

## Propósito
Integrar modelos de IA de forma segura, evaluable y compatible con determinismo Temporal, evitando que el LLM controle directamente el estado crítico.

## Resultados de aprendizaje

- Usar Spring AI para chat, structured output y tool calling con configuración externalizada.
- Diseñar prompts con contrato, contexto, límites y validación de salida.
- Implementar RAG básico con chunking, embeddings y vector store.
- Ejecutar llamadas de modelo y herramientas externas como Activities Temporal.
- Aplicar guardrails, evaluación, privacidad, costos y fallback.

## Desarrollo de los temas

- Modelos, tokens, temperatura, contexto y límites; no antropomorfizar.
- Spring AI ChatClient y providers; claves y modelos por configuración.
- Prompts y salida estructurada validada contra esquema/DTO.
- Tool/function calling con lista permitida y autorización en servidor.
- Embeddings, vector store, chunking, retrieval y citación de fuente.
- RAG vs fine-tuning conceptual; evaluación de groundedness.
- Temporal: llamada LLM en Activity; nunca HTTP/model call directo en Workflow.
- Integración temporal-spring-ai como opción de vista previa; APIs pueden cambiar.
- Seguridad: prompt injection, data leakage, output validation, rate/cost limits.

## Guion de presentación

- 1. IA como componente probabilístico dentro de un sistema determinista.
- 2. Arquitectura Spring AI y provider-neutrality.
- 3. Demo structured output a DTO validado.
- 4. Tool calling con allowlist.
- 5. RAG: ingestión→embedding→retrieval→respuesta.
- 6. Activity LLM con retry limitado e idempotencia de negocio.
- 7. Prompt injection y datos sensibles.
- 8. Evaluación y decisión de uso en proyecto.

## Secuencia temporal

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

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C14-E01 — Clasificador estructurado | Prompt contract, 15 min | Diseñar prompt que clasifique solicitud y devuelva DTO con categoría, urgencia y explicación breve. | **Entregable:** Prompt, DTO y parser. **Criterios:** Salida validada; valores fuera de enum rechazados. |
| C14-E02 — Fallback sin IA | Robustez, 15 min | Ante timeout o salida inválida, usar clasificación determinista simple. | **Entregable:** Servicio y tests. **Criterios:** La operación crítica continúa; fallo de IA es observable. |
| C14-E03 — Consulta de catálogo | Tool calling, 20 min | Exponer herramienta read-only para consultar recursos; el modelo no modifica DB. | **Entregable:** Tool y pruebas. **Criterios:** Allowlist; valida parámetros; autorización en backend. |
| C14-E04 — Asistente de normativa | RAG, 25 min | Ingerir 3 documentos, recuperar fragmentos y responder con referencias internas. | **Entregable:** Pipeline RAG y ejemplos. **Criterios:** Respuesta distingue “no encontrado”; muestra fuente/chunk. |
| C14-E05 — Prompt injection lab | Security, 20 min | Probar documentos que intentan cambiar instrucciones y mitigar mediante separación de roles/allowlist. | **Entregable:** Casos de ataque y defensas. **Criterios:** No ejecuta herramienta no autorizada ni revela prompt/secretos. |
| C14-E06 — Análisis durable | Temporal Activity, 25 min | Llamar al modelo desde Activity con timeout, retry limitado y registro de modelo/promptVersion. | **Entregable:** Workflow/Activity y tests mock. **Criterios:** No model call en Workflow; error permanente no se reintenta sin límite. |
| C14-E07 — Conjunto dorado | Evaluation, 18 min | Crear 20 preguntas/respuestas esperadas y medir exactitud, abstención y fuentes. | **Entregable:** eval dataset y reporte. **Criterios:** Métricas definidas; casos fallidos analizados. |
| C14-E08 — Presupuesto de tokens | Cost/latency, 18 min | Comparar dos configuraciones y establecer límites de tokens/latencia/costo simulado. | **Entregable:** Informe y configuración. **Criterios:** Decisión basada en datos y calidad mínima. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C14-T01 — Asistente SIGEO | 60-90 min | Implementar ayuda contextual con structured output y RAG sobre documentación del sistema. | **Entregable:** Módulo AI y 25 casos de evaluación. **Criterios:** No toma decisiones irreversibles; fuentes visibles. |
| C14-T02 — AI Activity resiliente | 60-90 min | Integrar llamada de IA como Activity con retry, timeout, fallback y trazabilidad de versión. | **Entregable:** Workflow y tests. **Criterios:** Determinismo preservado; payloads minimizados. |
| C14-T03 — Red-team de prompts | 60-90 min | Crear 15 ataques de inyección/exfiltración/tool abuse y registrar mitigaciones. | **Entregable:** docs/ai-red-team.md. **Criterios:** Severidad, evidencia y prueba regresiva. |
| C14-T04 — Guía visita profesional | 45-60 min | Preparar 12 preguntas sobre arquitectura, seguridad, DevOps, mensajería, Temporal/alternativas e IA responsable. | **Entregable:** docs/visita-profesional.md. **Criterios:** Preguntas abiertas y vinculadas al curso. |

## Evidencia de salida
Una llamada de IA se ejecuta como Activity, structured output se valida y el sistema demuestra fallback ante fallo o inyección.

## Preparación del docente
Preparar provider configurable o modelo stub, conjunto pequeño de documentos y vector store de laboratorio. No usar claves reales en material compartido.


---

# Clase 15: Middleware de mensajes, colas y procesamiento asíncrono

**Fecha:** 05-11-2026  
**Bloque:** Bloque 4 — IA y tecnologías avanzadas

## Propósito
Diseñar integración asíncrona mediante broker, comprender sus garantías y combinar mensajería con Temporal sin duplicar responsabilidades.

## Resultados de aprendizaje

- Explicar productor, consumidor, exchange/topic, queue, ack, redelivery y dead-letter queue.
- Implementar publicación/consumo con RabbitMQ y Spring AMQP (o equivalente institucional).
- Hacer consumidores idempotentes y controlar reintentos/DLQ.
- Aplicar outbox/inbox para consistencia con base de datos.
- Distinguir cuándo usar cola, evento, Temporal Workflow o combinación.

## Desarrollo de los temas

- Middleware: JMS conceptual, RabbitMQ y Kafka comparados por patrón, no por moda.
- At-most-once, at-least-once y “exactly-once” acotado; duplicados reales.
- Routing, durable queues, ack/nack, prefetch y backpressure.
- Retry en consumidor vs broker; DLQ y poison messages.
- Esquemas/versionado de mensajes y compatibilidad.
- Outbox transaccional e inbox/deduplication.
- Temporal + mensajería: mensajes como Activities/Signals; evitar doble orquestador.
- Validación Bean Validation, interceptores/AOP para observabilidad.
- Debrief de visita profesional y comparación con prácticas observadas.

## Guion de presentación

- 1. Debrief de visita profesional: hallazgos y contraste.
- 2. Broker y flujo de mensaje.
- 3. Demo RabbitMQ con publisher/consumer.
- 4. Ack, redelivery y duplicados.
- 5. DLQ y poison message.
- 6. Outbox/inbox y consistencia.
- 7. Temporal vs cola: matriz de decisión.
- 8. Operabilidad y seguridad del broker.

## Secuencia temporal

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–15 | Debrief visita profesional | Recoger hallazgos en tablero: arquitectura, seguridad y operación. |
| 15–40 | Fundamentos de mensajería | Dibujar rutas, garantías y fallos. |
| 40–65 | Demo broker | Publicar, consumir, fallar y redeliver. |
| 65–85 | Ejercicios E01–E03 | Publisher, consumer y validación. |
| 85–100 | Receso | Preparar DLQ/outbox. |
| 100–125 | Idempotencia, DLQ y outbox | Mostrar duplicado y poison message. |
| 125–165 | Laboratorio E04–E06 | Flujo robusto e integración Temporal. |
| 165–185 | Desafíos E07–E08 | Schema evolution y observabilidad. |
| 185–195 | Cierre y tarea | Matriz de decisión cola/Temporal. |

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C15-E01 — RabbitMQ local | Broker setup, 15 min | Levantar broker con Docker Compose y verificar management UI/health. | **Entregable:** compose y comandos. **Criterios:** Credenciales de laboratorio externalizadas; volumen/puertos documentados. |
| C15-E02 — Notificación asíncrona | Producer/consumer, 18 min | Publicar NotificationRequested y consumirlo con ack manual o configurado. | **Entregable:** Apps/beans y evidencia. **Criterios:** Mensaje tipado; correlationId; no pérdida en caso normal. |
| C15-E03 — Mensaje inválido | Validation, 15 min | Validar payload y enviar inválidos a ruta definida. | **Entregable:** Bean Validation y tests. **Criterios:** No entra en retry infinito por error de esquema. |
| C15-E04 — Duplicado de notificación | Idempotent consumer, 20 min | Persistir messageId procesado y evitar segunda notificación. | **Entregable:** Inbox y test duplicado. **Criterios:** Mismo mensaje produce un efecto. |
| C15-E05 — Poison message | DLQ, 20 min | Configurar retry limitado y DLQ; reprocess manual controlado. | **Entregable:** Configuración y runbook. **Criterios:** Mensaje problemático no bloquea cola principal. |
| C15-E06 — Publicación confiable | Outbox, 25 min | Guardar cambio y evento outbox en una transacción; publicador envía y marca. | **Entregable:** Tablas, job/activity y tests. **Criterios:** No existe ventana commit-sin-evento; publicación idempotente. |
| C15-E07 — Mensaje inicia o señala Workflow | Temporal bridge, 20 min | Consumidor usa WorkflowClient para start/update/signal con ID de negocio. | **Entregable:** Bridge y tests. **Criterios:** Redelivery no duplica Workflow ni comando. |
| C15-E08 — Evento v1→v2 | Schema evolution, 18 min | Agregar campo compatible y consumidor tolerante a versiones. | **Entregable:** Contratos y tests. **Criterios:** Consumidor antiguo no se rompe; cambios incompatibles versionados. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C15-T01 — Pipeline de notificaciones | 60-90 min | Outbox→RabbitMQ→consumer idempotente→auditoría con DLQ. | **Entregable:** Sistema y 25 pruebas. **Criterios:** Reinicio/redelivery no duplica; métricas básicas. |
| C15-T02 — Integración Workflow-broker | 60-90 min | Al completar saga, publicar evento; otro consumidor actualiza Workflow relacionado. | **Entregable:** Implementación y diagrama. **Criterios:** Responsabilidades claras; no hay ciclo infinito. |
| C15-T03 — Chaos de mensajería | 60-90 min | Simular broker caído, consumidor caído, duplicado, poison y mensaje fuera de orden. | **Entregable:** Informe y pruebas/scripts. **Criterios:** Estado recuperable y procedimientos documentados. |
| C15-T04 — Informe visita profesional | 60-90 min | Relacionar 5 observaciones de la visita con decisiones del proyecto. | **Entregable:** docs/visita-reflexion.md. **Criterios:** Distingue observación, interpretación y acción aplicable. |

## Evidencia de salida
Mensaje duplicado y poison message manejados correctamente; evento de negocio inicia/señala Workflow sin duplicarlo.

## Preparación del docente
Preparar Docker Compose RabbitMQ, colas/DLQ y datos de la visita profesional. Mantener alternativa conceptual Kafka/JMS sin exigir otra infraestructura.


---

# Clase 16: Proyecto integrador: requisitos, arquitectura y plan de construcción

**Fecha:** 12-11-2026  
**Bloque:** Bloque 5 — Integración final

## Propósito
Diseñar un producto integrador realizable que combine Spring Boot, PostgreSQL, seguridad, Temporal, mensajería e IA opcional con criterios verificables.

## Resultados de aprendizaje

- Definir alcance mínimo y extensiones sin convertir el proyecto en una colección de tecnologías.
- Modelar dominio, API, esquema, Workflow/Saga, mensajes y amenazas.
- Definir pruebas, SLO, observabilidad y estrategia de despliegue local.
- Descomponer en issues verticales y asignar responsabilidades.
- Producir un walking skeleton ejecutable al final de la sesión.

## Desarrollo de los temas

- Caso base: gestión de solicitudes con aprobación, asignación de recursos y notificaciones.
- Requisitos funcionales/no funcionales y criterios de aceptación.
- Arquitectura C4: contexto, contenedores y componentes.
- Dominio y base de datos; límites entre entidad JPA, DTO y estado Workflow.
- Workflow/Saga, Activities, Signals/Updates/Queries, mensajes y outbox.
- Seguridad y threat model; roles, secretos y datos en History.
- Estrategia de pruebas, replay, chaos y evaluación IA.
- Backlog, Definition of Done, branching y CI local.

## Guion de presentación

- 1. Enunciado del proyecto y entregables obligatorios.
- 2. Ejemplo de alcance mínimo vs sobrealcance.
- 3. Plantilla C4 y secuencia crítica.
- 4. Diseño Workflow/Saga y compensaciones.
- 5. Modelo de datos y contratos API/eventos.
- 6. Threat model y observabilidad.
- 7. Backlog vertical y criterios de aceptación.
- 8. Walking skeleton y gate de arquitectura.

## Secuencia temporal

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–15 | Presentación del caso y rúbrica | Aclarar mínimo obligatorio y extensiones. |
| 15–40 | Taller de requisitos | Equipos escriben historias y criterios. |
| 40–65 | Diseño C4/dominio | Revisión docente por mesa. |
| 65–85 | Ejercicios E01–E03 | Arquitectura, datos y Workflow. |
| 85–100 | Receso | Revisar riesgos de alcance. |
| 100–125 | Seguridad, pruebas y observabilidad | Completar threat model y test strategy. |
| 125–165 | Laboratorio E04–E06 | Backlog y walking skeleton. |
| 165–185 | Desafíos E07–E08 | ADR y demo de inicio end-to-end. |
| 185–195 | Gate de salida | Aprobar o devolver diseño con acciones concretas. |

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C16-E01 — Historias verticales | Requisitos, 20 min | Escribir 8 historias con criterios Given/When/Then y prioridad MoSCoW. | **Entregable:** backlog.md. **Criterios:** Cada historia produce valor observable y tiene criterio verificable. |
| C16-E02 — Diagrama C4 | Arquitectura, 20 min | Crear contexto/contenedores con Spring, DB, Temporal, broker y proveedor IA. | **Entregable:** Mermaid/PlantUML. **Criterios:** Responsabilidades y protocolos explícitos; no “caja mágica”. |
| C16-E03 — Secuencia crítica | Workflow design, 20 min | Definir estados, Activities, mensajes, timeouts, retries y compensaciones. | **Entregable:** workflow-design.md. **Criterios:** Determinismo y idempotencia revisados. |
| C16-E04 — Contratos mínimos | Data/API, 20 min | Diseñar tablas, endpoints, errores y eventos v1. | **Entregable:** OpenAPI + migración inicial + schema evento. **Criterios:** IDs/correlación coherentes; datos sensibles clasificados. |
| C16-E05 — Threat model del proyecto | Security, 18 min | DFD, trust boundaries y top 8 riesgos. | **Entregable:** threat-model.md. **Criterios:** Mitigaciones asignadas a historias. |
| C16-E06 — Plan de verificación | Testing, 18 min | Matriz requisito→tipo de prueba→fixture→evidencia. | **Entregable:** test-plan.md. **Criterios:** Incluye replay, chaos, security y AI eval si aplica. |
| C16-E07 — Issues y Definition of Done | Planning, 18 min | Crear issues de 30–90 min con dependencia y dueño. | **Entregable:** Issue board/export. **Criterios:** Ningún issue “hacer backend completo”; DoD incluye pruebas/docs. |
| C16-E08 — Camino mínimo | Walking skeleton, 25 min | POST inicia Workflow, una Activity guarda/consulta y GET muestra estado. | **Entregable:** Commit ejecutable. **Criterios:** Arranca con un comando; health de app/DB/Temporal visible. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C16-T01 — Sprint 1 | 60-90 min | Implementar dominio, DB, API inicial y seguridad base. | **Entregable:** Release candidate 0.1. **Criterios:** CI local verde; 30 pruebas; demo de 3 min. |
| C16-T02 — Sprint 2 | 60-90 min | Implementar Workflow/Saga con Activities idempotentes y consultas. | **Entregable:** Release candidate 0.2. **Criterios:** Fault injection en al menos 3 pasos. |
| C16-T03 — Sprint 3 | 60-90 min | Integrar mensajería y, opcionalmente, IA con evaluación/fallback. | **Entregable:** Release candidate 0.3. **Criterios:** DLQ/dedupe o AI guardrails demostrables. |
| C16-T04 — Documentación de entrega | 60-90 min | Completar README, diagramas, ADR, runbook, threat model y matriz de pruebas. | **Entregable:** docs release. **Criterios:** Un tercero puede levantar y probar el sistema. |

## Evidencia de salida
Diseño aprobado y walking skeleton ejecutable desde un repositorio limpio.

## Preparación del docente
Crear repositorio base, rúbrica, plantillas de ADR/C4/threat model/issues y script de bootstrap.


---

# Clase 17: Proyecto integrador: implementación, hardening y ensayo de defensa

**Fecha:** 19-11-2026  
**Bloque:** Bloque 5 — Integración final

## Propósito
Finalizar un incremento demostrable, someterlo a fallos y preparar una defensa técnica basada en evidencia.

## Resultados de aprendizaje

- Completar ruta crítica end-to-end con seguridad y persistencia.
- Ejecutar pruebas de carga ligera, caos, seguridad, replay y recuperación.
- Resolver deuda crítica sin reescrituras tardías.
- Preparar demo reproducible con fallos controlados y observabilidad.
- Defender decisiones arquitectónicas y limitaciones.

## Desarrollo de los temas

- Triage por riesgo y ruta crítica.
- Hardening de timeouts, retries, idempotencia, DLQ, compensaciones y cancelación.
- Pruebas end-to-end, replay, concurrency y security regression.
- Operabilidad: health, métricas, logs, Search Attributes y runbook.
- Performance básico: latencia, pool/virtual threads fuera de Workflow y límites.
- Demo script, datos semilla, reset y evidencias.
- Calidad del repositorio: commits, README, sin secretos, licencia/dependencias.
- Defensa: trade-offs, fallos conocidos y roadmap.

## Guion de presentación

- 1. Gate de completitud y riesgos P0/P1/P2.
- 2. Clínica de errores por equipo.
- 3. Checklist Temporal y mensajería.
- 4. Security regression y secretos.
- 5. Replay/chaos/concurrency tests.
- 6. Observabilidad durante una falla.
- 7. Estructura de demo y defensa.
- 8. Congelamiento de release candidate.

## Secuencia temporal

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

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C17-E01 — Bootstrap desde cero | Build gate, 15 min | Clonar en directorio limpio y levantar app, DB, Temporal y broker. | **Entregable:** Log de bootstrap. **Criterios:** Máximo 15 min; un comando o pasos claros; sin estado oculto. |
| C17-E02 — Ruta crítica | End-to-end, 20 min | Crear solicitud, autenticar, iniciar saga, aprobar, notificar y consultar. | **Entregable:** Prueba E2E/script. **Criterios:** Resultado verificable en API, DB, Temporal UI y broker. |
| C17-E03 — Fallo de proveedor | Chaos, 20 min | Detener servicio/Activity dependiente durante operación y recuperarlo. | **Entregable:** Demo y evidencia. **Criterios:** Workflow no se pierde; retry/compensación coherente. |
| C17-E04 — Compatibilidad de release | Replay, 20 min | Ejecutar replay de historias guardadas con código final. | **Entregable:** Reporte replay. **Criterios:** Cero nondeterminism o estrategia versionada documentada. |
| C17-E05 — Matriz de ataque | Security regression, 20 min | Ejecutar tokens inválidos, IDOR, mass assignment, secret scan y logs. | **Entregable:** Reporte. **Criterios:** Sin vulnerabilidad crítica abierta; excepciones justificadas. |
| C17-E06 — Redelivery/DLQ | Messaging, 20 min | Duplicar evento y enviar poison message durante demo. | **Entregable:** Evidencia broker. **Criterios:** No duplicación; DLQ y recuperación operativa. |
| C17-E07 — Diagnóstico en 5 minutos | Observability, 18 min | A partir de alerta simulada, localizar workflow/activity/mensaje y causa. | **Entregable:** Runbook ejecutado. **Criterios:** Usa correlationId, métricas/historia; no inspección manual aleatoria. |
| C17-E08 — Preguntas hostiles | Defense drill, 25 min | Responder trade-offs: Java 25, Boot 4, Temporal, saga, broker, IA y seguridad. | **Entregable:** Guion y feedback pares. **Criterios:** Respuesta con evidencia de código/prueba y limitaciones honestas. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C17-T01 — Release final | 60-90 min | Cerrar P0/P1, etiquetar v1.0.0 y generar paquete de entrega. | **Entregable:** Tag, release notes y checksum. **Criterios:** Build reproducible; tests verdes; no secretos. |
| C17-T02 — Video de contingencia | 60-90 min | Grabar demo de 8–10 min por si falla infraestructura el día del examen. | **Entregable:** Video y guion. **Criterios:** Incluye fallo/resiliencia, no solo happy path. |
| C17-T03 — Informe técnico | 60-90 min | Resumen de arquitectura, decisiones, pruebas, riesgos y trabajo futuro. | **Entregable:** Informe 6–10 páginas. **Criterios:** Coherente con repositorio; evidencia enlazada. |
| C17-T04 — Preparación individual | 60-90 min | Banco de 30 preguntas y respuestas breves; cada integrante domina todo el sistema. | **Entregable:** docs/defensa.md. **Criterios:** No repartir conocimiento en silos; respuestas verificables. |

## Evidencia de salida
Release candidate ejecutable, batería de fallos superada y defensa ensayada con evidencia.

## Preparación del docente
Preparar checklist automatizable, scripts de chaos/security, rúbrica y ambiente alternativo sin Internet.


---

# Clase 18: Examen final teórico-práctico y defensa del proyecto

**Fecha:** 26-11-2026  
**Bloque:** Bloque 5 — Evaluación final

## Propósito
Evaluar dominio conceptual y capacidad de construir, diagnosticar y defender una solución Java/Temporal resiliente.

## Resultados de aprendizaje

- Demostrar conocimientos individuales de Java, Spring, seguridad, persistencia, Temporal y mensajería.
- Resolver una modificación práctica bajo tiempo limitado con pruebas.
- Demostrar resiliencia, trazabilidad y recuperación en el proyecto.
- Defender decisiones y reconocer limitaciones sin depender de IA para responder.

## Desarrollo de los temas

- Examen escrito de conceptos y lectura de código.
- Práctico individual: cambio funcional + bug/fallo de resiliencia.
- Defensa del proyecto y demostración de fallo controlado.
- Revisión de repositorio, pruebas, seguridad y documentación.
- Reglas de uso de IA: puede estar deshabilitada o restringida según política institucional; toda respuesta debe ser defendida.

## Guion de presentación

- 1. Instrucciones, integridad académica y criterios.
- 2. Distribución de tiempo y entregables.
- 3. Ejemplo de evidencia válida.
- 4. Recordatorio de backup/branch de examen.
- 5. Procedimiento de demo y preguntas.
- 6. Cierre y entrega inmutable.

## Secuencia temporal

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Identificación y preparación | Verificar repositorios, ambiente y rama de examen. |
| 10–45 | Sección teórica individual | Preguntas de análisis, no memorización. |
| 45–115 | Práctico individual | Implementar cambio y corregir fallo con tests. |
| 115–125 | Pausa técnica | Guardar/commit; no continuar codificando. |
| 125–170 | Defensas y demostraciones | Turnos con falla controlada y preguntas. |
| 170–180 | Entrega final | Tag/commit hash, formulario y cierre. |

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C18-E01 — Análisis de arquitectura | Examen teórico, 35 min | Responder 12 ítems: POO, transacción, seguridad, determinismo, timeouts, saga, messaging e IA. | **Entregable:** Hoja individual. **Criterios:** Justificaciones y detección de antipatrones; no solo definiciones. |
| C18-E02 — Cambio de regla | Práctico, 25 min | Agregar nueva regla de prioridad con validación, persistencia y contrato HTTP. | **Entregable:** Commit de examen. **Criterios:** Pruebas unitarias/web; migración compatible si aplica. |
| C18-E03 — Nueva interacción | Práctico Temporal, 25 min | Agregar Update o Signal con validación y prueba time-skipping. | **Entregable:** Commit de examen. **Criterios:** Determinismo y backward compatibility considerados. |
| C18-E04 — Fallo inducido | Diagnóstico, 20 min | Corregir retry incorrecto o Activity no idempotente y añadir prueba regresiva. | **Entregable:** Fix y postmortem corto. **Criterios:** Causa raíz explicada; no esconder fallo. |
| C18-E05 — Demo resiliente | Defensa, 25 min | Demostrar happy path y un fallo recuperable/compensado. | **Entregable:** Demo en vivo o video contingencia. **Criterios:** Evidencia en UI/logs/DB; recuperación completa. |
| C18-E06 — Preguntas técnicas | Defensa individual, 20 min | Responder preguntas al azar y localizar código/prueba asociada. | **Entregable:** Evaluación oral. **Criterios:** Cada integrante comprende componentes principales. |
| C18-E07 — Higiene de entrega | Auditoría final, 15 min | Mostrar secret scan, dependencias, README, bootstrap y test report. | **Entregable:** Checklist firmado. **Criterios:** Sin secretos; build reproducible. |
| C18-E08 — Limitaciones y roadmap | Reflexión, 10 min | Declarar 3 límites reales y 3 mejoras priorizadas. | **Entregable:** Nota final. **Criterios:** No presentar prototipo como producción sin reservas. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C18-T01 — Corrección post-examen | 45-60 min | Revisar feedback y proponer corrección de errores sin modificar entrega evaluada. | **Entregable:** plan-mejora.md. **Criterios:** Vincula error, causa, conocimiento faltante y acción. |
| C18-T02 — Portafolio técnico | 60-90 min | Preparar versión pública anonimizada o dossier interno del proyecto. | **Entregable:** portfolio.md. **Criterios:** Sin datos/secretos institucionales; incluye arquitectura y aprendizajes. |
| C18-T03 — Autoevaluación | 30-45 min | Comparar desempeño con rúbrica y aportar evidencia. | **Entregable:** autoevaluacion.md. **Criterios:** Honesta y específica. |
| C18-T04 — Preparación de repetición | 60-120 min | Solo para quien corresponda: plan de estudio focalizado y ejercicios de remediación. | **Entregable:** plan-recuperacion.md. **Criterios:** Prioriza brechas demostradas. |

## Evidencia de salida
Entrega identificada por commit/tag, examen individual completo y defensa con demostración de resiliencia.

## Preparación del docente
Preparar repositorios de examen, variantes equivalentes, rúbrica, contingencia offline, tokens/stubs y hoja de identificación de commits.


---

# Clase 19: Examen de repetición, nivelación y cierre técnico

**Fecha:** 03-12-2026  
**Bloque:** Bloque 5 — Recuperación y cierre

## Propósito
Ofrecer evaluación equivalente, corregir brechas fundamentales y consolidar una ruta profesional de continuidad.

## Resultados de aprendizaje

- Resolver una variante equivalente del examen sin reutilizar respuestas.
- Demostrar corrección de las brechas identificadas.
- Analizar errores comunes del curso mediante ejemplos anonimizados.
- Cerrar el portafolio y plan de aprendizaje posterior.

## Desarrollo de los temas

- Examen de repetición con caso distinto y misma matriz de competencias.
- Clínica de determinismo, transacciones, seguridad e idempotencia.
- Revisión de código y postmortems anonimizados.
- Mantenimiento: actualización de dependencias, replay y migraciones.
- Ruta de aprendizaje: Java, Spring, Temporal, arquitectura distribuida y seguridad.

## Guion de presentación

- 1. Reglas y equivalencia de evaluación.
- 2. Distribución de tiempo.
- 3. Clínica posterior: top 10 errores del curso.
- 4. Cómo mantener el sistema seis meses después.
- 5. Portafolio y ruta profesional.
- 6. Cierre y retroalimentación.

## Secuencia temporal

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Preparación y variante asignada | Verificar ambiente y repositorio limpio. |
| 10–45 | Teórico de repetición | Ítems equivalentes con código/casos distintos. |
| 45–115 | Práctico de repetición | Cambio funcional + resiliencia + pruebas. |
| 115–125 | Pausa/entrega parcial | Commit obligatorio. |
| 125–155 | Defensa individual | Demo y preguntas. |
| 155–175 | Clínica de errores comunes | Revisar ejemplos anonimizados y correcciones. |
| 175–180 | Cierre | Entregar plan de continuidad y encuesta. |

## Ejercicios del repositorio para la clase

| ID | Tipo/tiempo | Especificación | Entregable y aceptación |
|---|---|---|---|
| C19-E01 — Análisis equivalente | Repetición teórica, 35 min | Resolver nueva matriz de 12 ítems con énfasis en brechas previas. | **Entregable:** Hoja individual. **Criterios:** Mismo nivel cognitivo; casos distintos. |
| C19-E02 — Cambio compatible | Práctico web/DB, 25 min | Agregar atributo/consulta con migración, validación y seguridad. | **Entregable:** Commit. **Criterios:** No rompe datos ni permisos; tests. |
| C19-E03 — Cancelación o compensación | Práctico Temporal, 25 min | Implementar cancelación segura o nueva compensación idempotente. | **Entregable:** Commit y test. **Criterios:** No I/O en Workflow; comportamiento ante retry/cancel demostrado. |
| C19-E04 — Nondeterminism o duplicate effect | Debug, 20 min | Diagnosticar y corregir uno de dos fallos asignados. | **Entregable:** Postmortem y prueba. **Criterios:** Causa raíz y prevención. |
| C19-E05 — Demostración técnica | Defensa, 20 min | Mostrar cambio, tests y fallo recuperado. | **Entregable:** Evaluación oral. **Criterios:** Evidencia directa y explicación clara. |
| C19-E06 — Errores comunes | Code clinic, 15 min | Corregir en grupo tres snippets: catch vacío, @Transactional mal ubicado y HTTP en Workflow. | **Entregable:** Soluciones comentadas. **Criterios:** Explica por qué, no solo corrige. |
| C19-E07 — Upgrade rehearsal | Mantenimiento, 15 min | Simular actualización de dependencia, ejecutar tests/replay y registrar riesgos. | **Entregable:** Checklist. **Criterios:** No actualizar a ciegas; rollback plan. |
| C19-E08 — Ficha de proyecto | Portafolio, 10 min | Redactar resumen técnico de una página con contribución individual. | **Entregable:** portfolio-one-pager.md. **Criterios:** Sin datos sensibles; enlaces/evidencias internas válidas. |

## Tarea para el hogar

| ID | Esfuerzo | Especificación | Entregable y aceptación |
|---|---:|---|---|
| C19-T01 — Plan 30-60-90 | 30-45 min | Definir práctica técnica para 30, 60 y 90 días. | **Entregable:** learning-plan.md. **Criterios:** Metas medibles y repositorios/proyectos concretos. |
| C19-T02 — Backlog de mantenimiento | 45-60 min | Crear 10 issues futuros priorizados por riesgo/valor. | **Entregable:** maintenance-backlog.md. **Criterios:** Incluye dependencias, seguridad, observabilidad y deuda. |
| C19-T03 — Lecciones aprendidas | 30-45 min | Escribir postmortem del proceso de aprendizaje y 5 prácticas que mantendrá. | **Entregable:** retrospectiva.md. **Criterios:** Ejemplos concretos del curso. |
| C19-T04 — Contribución final | 30-60 min | Corregir una mejora documental o prueba en el repositorio común, si la política institucional lo permite. | **Entregable:** Pull request. **Criterios:** Cambio pequeño, revisable y sin respuestas de examen. |

## Evidencia de salida
Variante de repetición evaluada, clínica de errores completada y plan 30-60-90 definido.

## Preparación del docente
Preparar variantes equivalentes, snippets anonimizados, rúbrica y material de continuidad.


## 7. Definition of Done para todo ejercicio

- El proyecto compila y ejecuta usando Maven Wrapper desde terminal.
- Las pruebas relevantes pasan y el alumno puede explicar qué verifican.
- No hay secretos ni datos sensibles en código, logs, fixtures o historia de Git.
- Los errores se manejan con mensajes/contexto útiles; no hay catch vacíos.
- El README contiene comandos exactos de ejecución y ejemplos.
- En Temporal, se cumple determinismo, timeouts, retry limitado e idempotencia.
- El commit identifica clase/ejercicio y el diff no contiene cambios ajenos.
- El alumno puede defender el código sin depender de la respuesta de una IA.

## 8. Fuentes técnicas de referencia verificadas al 29-07-2026

- Oracle Java SE Support Roadmap y documentación JDK 25.
- Spring Boot System Requirements 4.1.0.
- Temporal Java SDK Documentation: workflows, determinism, testing, Spring Boot integration, observability y cancellation.
- Temporal Java SDK GitHub Releases: v1.37.0 y soporte Spring Boot 4 desde v1.34.0.
- Temporal Spring AI integration documentation (Public Preview).