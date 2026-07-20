# Clase 03: Depuración, Pruebas y Documentación

## Objetivos de la sesión
- Comprender y aplicar técnicas de depuración moderna en aplicaciones Java.
- Escribir pruebas unitarias efectivas utilizando JUnit 5 y aserciones fluidas con AssertJ.
- Documentar código de manera profesional utilizando JavaDoc.
- Integrar herramientas de Inteligencia Artificial para la generación, análisis y mejora de pruebas automatizadas.

## Cronograma propuesto
- **0:00 - 0:45**: Introducción a la depuración moderna (breakpoints, evaluación de expresiones, depuración remota).
- **0:45 - 1:30**: Fundamentos de pruebas unitarias con JUnit 5 (anotaciones, ciclo de vida, pruebas parametrizadas).
- **1:30 - 1:45**: *Descanso*.
- **1:45 - 2:30**: Aserciones avanzadas y legibles con AssertJ.
- **2:30 - 3:15**: Buenas prácticas de documentación con JavaDoc y generación de reportes.
- **3:15 - 4:00**: Uso de IA (ChatGPT, Claude, Copilot) para la creación y refactorización de tests.

## Ejercicios prácticos

### Ejercicio 1: Guiado
**Configuración y Primeras Pruebas con JUnit 5 y AssertJ**
En este ejercicio, configuraremos un proyecto básico con Maven/Gradle, añadiremos las dependencias de JUnit 5 y AssertJ, y escribiremos pruebas para una clase `CalculadoraFinanciera`.

**Pasos:**
1. Crea un nuevo proyecto Java y añade las dependencias de `junit-jupiter` y `assertj-core`.
2. Crea la clase `CalculadoraFinanciera` con un método `calcularInteresCompuesto(double capital, double tasa, int anios)`.
3. Crea la clase de prueba `CalculadoraFinancieraTest`.
4. Escribe un test básico usando `@Test` y verifica el resultado usando `assertThat()` de AssertJ.
5. Ejecuta la prueba y verifica que pase. Introduce un error intencional y observa el mensaje de fallo de AssertJ.

**Asistencia de IA:**
- **Modo Chat (ChatGPT/Claude):** "Actúa como un profesor de Java. Explícame paso a paso cómo configurar JUnit 5 y AssertJ en un proyecto Maven y dame un ejemplo básico de un test para una calculadora."
- **Claude Code / Codex:** "Genera el archivo pom.xml con las dependencias de JUnit 5 y AssertJ. Luego, crea una clase CalculadoraFinanciera y su respectiva clase de prueba con un test básico."

### Ejercicio 2: Semi-guiado
**Depuración y Pruebas Parametrizadas**
Tienes una clase `ValidadorContrasenas` con un método `esValida(String password)` que contiene un bug lógico. Debes usar el depurador de tu IDE para encontrar el error, corregirlo y luego escribir pruebas parametrizadas para cubrir múltiples casos.

**Pistas:**
- El método falla cuando la contraseña tiene exactamente 8 caracteres y contiene un número, pero no caracteres especiales.
- Pon un breakpoint en la primera línea del método y evalúa las condiciones paso a paso.
- Usa `@ParameterizedTest` y `@CsvSource` de JUnit 5 para probar contraseñas válidas e inválidas.

**Asistencia de IA:**
- **Modo Chat (ChatGPT/Claude):** "Tengo este código Java para validar contraseñas que tiene un bug: [pegar código]. ¿Puedes darme pistas de dónde podría estar el error sin darme la solución directa? Luego, muéstrame cómo estructurar un @ParameterizedTest en JUnit 5 para probar varios casos."
- **Claude Code / Codex:** "Analiza el método esValida en ValidadorContrasenas.java, encuentra el bug lógico y sugiere una corrección. Después, genera pruebas parametrizadas usando @CsvSource para cubrir al menos 5 casos límite."

### Ejercicio 3: Desafío
**TDD, Documentación y Cobertura con IA**
Desarrolla un sistema de `GestorDeReservas` para un hotel utilizando Desarrollo Guiado por Pruebas (TDD). El sistema debe permitir reservar habitaciones, cancelar reservas y verificar disponibilidad.

**Requisitos:**
- Escribe las pruebas antes que el código de producción.
- Utiliza AssertJ para aserciones complejas (ej. verificar que una lista de reservas contiene ciertos elementos o extraer propiedades).
- Documenta todas las clases y métodos públicos utilizando JavaDoc, explicando los parámetros, valores de retorno y excepciones lanzadas.
- Utiliza una herramienta de IA para analizar la cobertura de tus pruebas y sugerir casos de prueba faltantes (edge cases).

**Asistencia de IA:**
- **Modo Chat (ChatGPT/Claude):** "Estoy practicando TDD en Java. Quiero crear un GestorDeReservas de hotel. ¿Puedes actuar como mi par de programación? Yo te daré mi primer test y tú me guiarás sobre qué código de producción escribir, y luego me sugerirás el siguiente test. Además, ayúdame a redactar el JavaDoc profesional para la clase."
- **Claude Code / Codex:** "Revisa la clase GestorDeReservasTest.java. Identifica casos límite (edge cases) que no estoy cubriendo, como reservas en fechas pasadas o solapamiento de fechas, y genera los tests correspondientes usando JUnit 5 y AssertJ. Finalmente, genera el JavaDoc para la clase GestorDeReservas.java."