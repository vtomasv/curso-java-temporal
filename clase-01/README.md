# Clase 01: Fundamentos de Java Moderno y Ecosistema

## Objetivos de la sesión
- Configurar el entorno de desarrollo para Java 21.
- Comprender y aplicar los conceptos fundamentales de la Programación Orientada a Objetos (POO) en Java.
- Utilizar características modernas de Java: Records y Pattern Matching.
- Introducir el concepto de Virtual Threads para concurrencia ligera.
- Integrar y utilizar agentes de Inteligencia Artificial (IA) como asistentes en el desarrollo de software.

## Cronograma propuesto
- **Hora 1:** Configuración del entorno (JDK 21, IDE) y primeros pasos con agentes de IA. Repaso de POO.
- **Hora 2:** Características modernas: Records y Pattern Matching. Ejemplos prácticos.
- **Hora 3:** Introducción a la concurrencia moderna con Virtual Threads.
- **Hora 4:** Desarrollo de ejercicios prácticos, resolución de dudas y uso de IA para refactorización y debugging.

## Ejercicios prácticos

### Ejercicio 1: Guiado
**Modelado de un Sistema de Biblioteca (Records y POO)**
Vamos a crear un sistema básico para gestionar libros en una biblioteca utilizando `records` para modelar los datos y clases tradicionales para la lógica de negocio.

**Pasos:**
1. Crea un `record` llamado `Libro` con los campos: `isbn` (String), `titulo` (String), `autor` (String) y `anioPublicacion` (int).
2. Crea una clase `Biblioteca` que contenga una lista de libros (`List<Libro>`).
3. Implementa métodos en `Biblioteca` para agregar un libro y buscar libros por autor.
4. En el método `main`, instancia la biblioteca, agrega un par de libros y realiza una búsqueda.

**Asistencia de IA:**
- **Modo Chat (ChatGPT/Claude):** "Actúa como un profesor de Java. Explícame paso a paso cómo crear un `record` en Java 21 para representar un Libro y cómo se diferencia de una clase tradicional (POJO)."
- **Instrucciones para Claude Code/Codex:** "Genera un record `Libro` con isbn, titulo, autor y anioPublicacion. Luego, crea una clase `Biblioteca` con una lista de libros y métodos para agregar y buscar por autor."

### Ejercicio 2: Semi-guiado
**Procesamiento de Formas con Pattern Matching**
Implementa un sistema que calcule el área de diferentes formas geométricas utilizando *Pattern Matching* para `switch` (característica de Java 21).

**Pistas:**
- Crea una interfaz sellada (`sealed interface`) llamada `Forma` que permita las implementaciones `Circulo` y `Rectangulo`.
- `Circulo` y `Rectangulo` pueden ser `records`.
- Crea un método `calcularArea(Forma forma)` que use un `switch` con *Pattern Matching* para devolver el área correspondiente.

**Asistencia de IA:**
- **Modo Chat:** "Tengo una interfaz sellada `Forma` en Java 21 con implementaciones `Circulo` y `Rectangulo`. ¿Cómo puedo escribir un método usando 'switch pattern matching' para calcular el área dependiendo del tipo de forma?"
- **Instrucciones para Claude Code/Codex:** "Completa este método `calcularArea(Forma forma)` usando switch pattern matching de Java 21 para manejar los casos de Circulo y Rectangulo."

### Ejercicio 3: Desafío
**Servidor Concurrente con Virtual Threads**
Crea un simulador de un servidor web que procese múltiples peticiones de forma concurrente. El objetivo es demostrar la eficiencia de los *Virtual Threads* frente a los hilos tradicionales (Platform Threads).

**Requisitos:**
- Simula una tarea que tome tiempo (ej. `Thread.sleep(1000)`).
- Lanza 10,000 tareas concurrentes.
- Implementa la solución primero usando un `ExecutorService` tradicional (ej. `Executors.newFixedThreadPool`) y mide el tiempo.
- Luego, implementa la misma solución usando `Executors.newVirtualThreadPerTaskExecutor()` y compara el rendimiento y uso de recursos.

**Asistencia de IA:**
- **Modo Chat:** "Quiero entender cómo funcionan los Virtual Threads en Java 21. ¿Puedes darme un ejemplo de cómo lanzar 10,000 tareas concurrentes usando `Executors.newVirtualThreadPerTaskExecutor()` y explicar por qué es mejor que usar un ThreadPool tradicional para tareas bloqueantes?"
- **Instrucciones para Claude Code/Codex:** "Escribe un programa en Java 21 que compare el tiempo de ejecución de 10,000 tareas (cada una con un sleep de 1 segundo) usando un FixedThreadPool de 100 hilos vs un VirtualThreadPerTaskExecutor."