# Clase 02: Características Avanzadas y Manejo de Errores

## Objetivos de la sesión
- Comprender y aplicar el manejo avanzado de excepciones en Java.
- Utilizar la estructura `try-with-resources` para la gestión automática de recursos.
- Implementar un sistema de logging efectivo utilizando SLF4J y Logback.
- Entender y aplicar clases anónimas y expresiones lambda.
- Comprender los conceptos básicos del Garbage Collector (GC) en Java.

## Cronograma propuesto (4 horas)
- **Hora 1:** Manejo avanzado de excepciones y `try-with-resources`. Teoría y ejemplos prácticos.
- **Hora 2:** Introducción a SLF4J y Logback. Configuración y buenas prácticas de logging.
- **Hora 3:** Clases anónimas y expresiones lambda. Evolución del código y programación funcional básica.
- **Hora 4:** Conceptos de Garbage Collector (GC). Resolución de ejercicios prácticos y dudas.

## Ejercicios prácticos

### Ejercicio 1: Guiado - Lector de Archivos Seguro con Logging
**Descripción:** Crear un programa que lea un archivo de texto, cuente las palabras y registre el proceso utilizando SLF4J/Logback. Se debe usar `try-with-resources` para asegurar el cierre del archivo y manejar posibles excepciones (ej. archivo no encontrado).

**Pasos:**
1. Configurar un proyecto Maven/Gradle y añadir las dependencias de SLF4J y Logback.
2. Crear el archivo `logback.xml` para configurar la salida de logs por consola.
3. Crear una clase `LectorArchivos` con un método que reciba la ruta del archivo.
4. Implementar `try-with-resources` con `BufferedReader` y `FileReader`.
5. Añadir logs de nivel INFO al iniciar la lectura, DEBUG por cada línea leída, y ERROR si ocurre una excepción.

**Asistencia de IA:**
- *Prompt para Chat:* "Actúa como un profesor de Java. Ayúdame a configurar SLF4J y Logback en un proyecto Maven paso a paso. ¿Qué dependencias necesito y cómo creo un archivo logback.xml básico?"
- *Prompt para Claude Code/Codex:* "Genera una clase Java llamada LectorArchivos que use try-with-resources para leer un archivo de texto. Incluye un logger de SLF4J para registrar el inicio de la lectura (INFO) y cualquier IOException (ERROR)."

### Ejercicio 2: Semi-guiado - Procesador de Datos con Lambdas
**Descripción:** Tienes una lista de objetos `Transaccion` (id, monto, estado). Debes filtrar las transacciones completadas, aplicarles un impuesto del 15% y mostrar el resultado. Utiliza expresiones lambda en lugar de clases anónimas.

**Pistas:**
- Crea la clase `Transaccion` con sus atributos, constructor y getters.
- Usa `List.of(...)` para crear datos de prueba.
- Puedes usar el método `removeIf()` de las colecciones o la API de Streams (`stream().filter(...).forEach(...)`) con lambdas.
- Recuerda que las lambdas simplifican la implementación de interfaces funcionales.

**Asistencia de IA:**
- *Prompt para Chat:* "Tengo una lista de objetos Transaccion en Java. ¿Cómo puedo usar expresiones lambda y la API de Streams para filtrar solo las transacciones con estado 'COMPLETADA' y luego imprimir sus montos?"
- *Prompt para Claude Code/Codex:* "Refactoriza este código que usa clases anónimas para filtrar una lista, convirtiéndolo para que use expresiones lambda y Streams de Java 8+."

### Ejercicio 3: Desafío - Sistema de Monitoreo de Recursos Simulados
**Descripción:** Diseña un sistema que simule la conexión a múltiples bases de datos. Cada conexión debe ser un recurso que implemente `AutoCloseable`. El sistema debe intentar conectar a 3 bases de datos; si una falla, debe lanzar una excepción personalizada `ConexionFallidaException`. Utiliza logging intensivo para registrar la creación de objetos (para discutir el impacto en el GC) y el cierre automático de recursos.

**Requisitos:**
- Interfaz o clase abstracta `RecursoBaseDatos` que implemente `AutoCloseable`.
- Excepción personalizada `ConexionFallidaException`.
- Uso de `try-with-resources` múltiple (declarando varios recursos en el mismo try).
- Logs detallados (TRACE, DEBUG, INFO, WARN, ERROR) simulando el ciclo de vida de la conexión.
- Provocar intencionalmente un error en una conexión para ver cómo se cierran automáticamente los recursos que sí se abrieron.

**Asistencia de IA:**
- *Prompt para Chat:* "Quiero crear un desafío en Java donde simulo conexiones a bases de datos que implementan AutoCloseable. ¿Me puedes dar ideas de cómo estructurar una excepción personalizada y cómo se ve un try-with-resources declarando múltiples recursos a la vez?"
- *Prompt para Claude Code/Codex:* "Crea una clase simulada ConexionBD que implemente AutoCloseable. En su constructor, que tenga un 30% de probabilidad de lanzar una ConexionFallidaException (excepción personalizada). Añade logs de SLF4J en el constructor y en el método close(). Luego, escribe un método main que intente instanciar 3 de estas conexiones en un solo bloque try-with-resources."