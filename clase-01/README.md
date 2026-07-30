# Clase 1: Java 25 LTS, ecosistema Java y entorno profesional

**Bloque:** Bloque 1 — Fundamentos de Java moderno  
**Duración:** 4 horas

## Objetivos de Aprendizaje

- Instalar y verificar JDK 25 LTS, Git, Docker, Maven Wrapper y extensiones de Java para VS Code.
- Explicar bytecode, JVM, JDK, classpath, módulos y empaquetado JAR/WAR a nivel introductorio.
- Escribir programas con variables, tipos, operadores, control de flujo, métodos y entrada por consola.
- Comparar el despliegue en servidores Jakarta EE open source (WildFly, Open Liberty, Payara) con aplicaciones Spring Boot autocontenidas.
- Usar IA como asistente verificable: pedir, revisar, ejecutar pruebas y registrar el prompt utilizado.

## Cronograma de la Clase

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

## Ejercicios de Clase

### C01-E01 — Auditoría del entorno
**Especificación:** Crear un script o documento que capture versiones de Java, Maven Wrapper, Git y Docker, y valide que el proyecto compila.
**Entregable y aceptación:** `env-report.md` y salida de `./mvnw test`. Todas las herramientas verificadas; sin rutas personales ni secretos.
**Archivos involucrados:** `env-report.md`
**Comando para verificar:** `./mvnw test`

### C01-E02 — Conversor de unidades
**Especificación:** Aplicación de consola que convierta temperatura y distancia, con validación de opción y formato de salida estable.
**Entregable y aceptación:** `ConversorUnidades.java` y tres ejecuciones de ejemplo. Cubre entradas válidas, opción desconocida y redondeo definido.
**Archivos involucrados:** `ConversorUnidades.java`, `ConversorUnidadesTest.java`
**Comando para verificar:** `./mvnw test -Dtest=ConversorUnidadesTest`

### C01-E03 — Clasificador de prioridad
**Especificación:** Usar switch expression para convertir un código 1–5 en prioridad y plazo máximo.
**Entregable y aceptación:** Método `priorityFor(int)` y pruebas parametrizadas mínimas. No usar cadena de if; lanza error controlado fuera de rango.
**Archivos involucrados:** `ClasificadorPrioridad.java`, `ClasificadorPrioridadTest.java`
**Comando para verificar:** `./mvnw test -Dtest=ClasificadorPrioridadTest`

### C01-E04 — Solicitud operativa
**Especificación:** Definir un record `Solicitud` con id, solicitante, descripción, prioridad y fecha; imprimir resumen legible.
**Entregable y aceptación:** Record, factoría y demo. Inmutabilidad; validación de campos obligatorios.
**Archivos involucrados:** `Solicitud.java`, `SolicitudTest.java`
**Comando para verificar:** `./mvnw test -Dtest=SolicitudTest`

### C01-E05 — De script a métodos
**Especificación:** Recibir un programa monolítico y separar lectura, validación, cálculo y presentación.
**Entregable y aceptación:** Commit antes/después y explicación de responsabilidades. Métodos pequeños; no duplicación; comportamiento preservado.
**Archivos involucrados:** `ProcesadorMonolitico.java`, `ProcesadorMonoliticoTest.java`
**Comando para verificar:** `./mvnw test -Dtest=ProcesadorMonoliticoTest`

### C01-E06 — Menú SIGEO v0
**Especificación:** Construir menú alta/listado/búsqueda/salida usando una lista en memoria y bucle de aplicación.
**Entregable y aceptación:** Proyecto ejecutable con datos de ejemplo. No termina ante entrada inválida; salida consistente.
**Archivos involucrados:** `MenuSigeo.java`, `MenuSigeoTest.java`
**Comando para verificar:** `./mvnw test -Dtest=MenuSigeoTest`

### C01-E07 — Empaquetado reproducible
**Especificación:** Configurar manifest y generar JAR ejecutable; ejecutar fuera del IDE.
**Entregable y aceptación:** JAR y comando documentado. Funciona desde terminal limpia usando Maven Wrapper.
**Archivos involucrados:** `pom.xml`
**Comando para verificar:** `./mvnw clean package && java -jar target/ejercicios-1.0-SNAPSHOT.jar`

### C01-E08 — Auditor de código generado
**Especificación:** Pedir a una IA dos soluciones al mismo problema, comparar legibilidad, errores y complejidad, y elegir una con argumentos.
**Entregable y aceptación:** `ia-review.md` con prompt, diferencias y decisión. Incluye al menos un defecto detectado y una prueba añadida para cubrirlo.
**Archivos involucrados:** `ia-review.md`
**Comando para verificar:** Revisión manual del documento.

## Tareas para el Hogar

### C01-T01 — Calculadora de plazos
**Especificación:** CLI con alta de fecha y prioridad que calcule fecha límite, evitando duplicación de lógica.
**Entregable y aceptación:** Proyecto Maven y README con casos. Al menos 8 pruebas, incluyendo límites de mes.

### C01-T02 — Mapa del ecosistema
**Especificación:** Crear diagrama Mermaid que relacione Java SE, Jakarta EE, Spring Boot, servidor de aplicaciones, base de datos, Temporal y broker.
**Entregable y aceptación:** `docs/ecosistema.md`. Flechas y responsabilidades correctas; máximo una página.

### C01-T03 — Guía de instalación del equipo
**Especificación:** Documentar instalación limpia en Windows o Linux con comandos de verificación y solución de 3 fallos frecuentes.
**Entregable y aceptación:** `docs/setup.md`. Reproducible por un compañero.

### C01-T04 — Bitácora de IA
**Especificación:** Resolver una kata adicional con IA y registrar prompt inicial, correcciones, pruebas y reflexión.
**Entregable y aceptación:** `docs/ia-log-c01.md`. No se acepta solo código final; debe mostrar validación crítica.

## Cómo Ejecutar

Para compilar y ejecutar las pruebas de los ejercicios:
```bash
./mvnw clean test
```

Para empaquetar la aplicación:
```bash
./mvnw clean package
```

Para ejecutar el JAR generado (asegúrate de configurar la clase principal en el `pom.xml`):
```bash
java -jar target/ejercicios-1.0-SNAPSHOT.jar
```
