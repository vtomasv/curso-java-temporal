# Clase 10: Actividades y Manejo de Fallos en Temporal

## Objetivos de la sesión
* Comprender el rol de las Actividades (Activities) dentro de los flujos de trabajo (Workflows) en Temporal.
* Implementar y configurar correctamente las Actividades utilizando Java y Spring Boot.
* Dominar el manejo de fallos mediante políticas de reintento (Retry Policies) y tiempos de espera (Timeouts).
* Aprender a capturar y gestionar excepciones específicas de Temporal en Workflows y Actividades para construir sistemas resilientes.

## Cronograma propuesto (4 horas)
* **Hora 1:** Introducción a las Actividades en Temporal. Diferencias fundamentales entre Workflows (deterministas) y Actividades (no deterministas). Restricciones y buenas prácticas.
* **Hora 2:** Configuración profunda de Timeouts (`ScheduleToClose`, `StartToClose`, `ScheduleToStart`, `Heartbeat`) y diseño de Retry Policies.
* **Hora 3:** Manejo de excepciones. Propagación de errores desde Actividades hacia Workflows (`ActivityFailure`, `ApplicationFailure`).
* **Hora 4:** Desarrollo de ejercicios prácticos, resolución de dudas, revisión de código y patrones de compensación de fallos.

## Ejercicios prácticos

### Ejercicio 1: Guiado - Implementación de una Actividad Básica con Reintentos
**Descripción:** En este ejercicio, crearemos un Workflow que invoca una Actividad encargada de simular una llamada a una API externa que falla de manera intermitente. Configuraremos una política de reintentos para que Temporal maneje los fallos automáticamente sin intervención manual.

**Pasos:**
1. Define la interfaz de la Actividad anotada con `@ActivityInterface` y un método `llamarApiExterna()`.
2. Crea la implementación de la interfaz. Dentro del método, genera un número aleatorio y lanza una `RuntimeException` si el número es menor a 0.5 (simulando un fallo del 50%).
3. En la implementación del Workflow, configura las `ActivityOptions` estableciendo un `StartToCloseTimeout` de 5 segundos y un `RetryOptions` con un máximo de 3 intentos (`setMaximumAttempts(3)`).
4. Invoca la Actividad desde el Workflow, registra el Worker y observa los logs en la consola para ver cómo Temporal ejecuta los reintentos automáticamente.

**Asistencia de IA:**
* *Modo Chat (ChatGPT/Claude):* "Soy principiante en Temporal con Java. ¿Puedes mostrarme paso a paso cómo definir una interfaz de Actividad y configurarle un RetryOptions básico dentro de un Workflow?"
* *Claude Code / Codex:* "Genera una interfaz de Actividad en Temporal llamada `ExternalApiActivity` y su implementación en Spring Boot que lance una `RuntimeException` el 50% de las veces. Luego, crea un Workflow que la invoque con un máximo de 3 reintentos."

### Ejercicio 2: Semi-guiado - Manejo de Timeouts y Excepciones Personalizadas
**Descripción:** Extiende el ejercicio anterior. Ahora la Actividad simulará un proceso largo (usando `Thread.sleep`). Debes configurar los timeouts adecuadamente y manejar una excepción de negocio específica que no debe ser reintentada.

**Pistas:**
* Investiga y usa `ScheduleToCloseTimeout` y `StartToCloseTimeout`. ¿Cuál es la diferencia entre el tiempo de ejecución de la actividad y el tiempo total incluyendo encolamiento y reintentos?
* Crea una excepción personalizada, por ejemplo, `InvalidRequestException`.
* En `RetryOptions`, utiliza el método `setDoNotRetry` pasándole el tipo de tu excepción personalizada para evitar que Temporal reintente si se lanza este error específico.
* Captura la excepción en el Workflow usando un bloque `try-catch` atrapando `ActivityFailure` y toma una acción compensatoria (ej. registrar el error o retornar un estado de fallo controlado).

**Asistencia de IA:**
* *Modo Chat:* "En el SDK de Java para Temporal, ¿cómo configuro un `RetryOptions` para que NO reintente si la Actividad lanza una excepción específica llamada `InvalidRequestException`?"
* *Claude Code / Codex:* "Actualiza la configuración de `ActivityOptions` en mi código para incluir un `StartToCloseTimeout` de 2 segundos y evitar reintentos para la excepción `InvalidRequestException`. Muestra cómo capturar correctamente el `ActivityFailure` en el Workflow."

### Ejercicio 3: Desafío - Sistema de Procesamiento de Pagos con Compensación (Saga Pattern Básico)
**Descripción:** Diseña e implementa un Workflow de procesamiento de pagos de comercio electrónico que involucre tres Actividades: `reservarInventario`, `procesarPago` y `confirmarOrden`. Si `procesarPago` falla de forma definitiva (después de agotar sus reintentos o por un error fatal), el Workflow debe ejecutar una Actividad de compensación llamada `cancelarReservaInventario`.

**Requisitos:**
* Cada Actividad debe tener sus propios Timeouts y Retry Policies adaptados a su naturaleza (ej. el pago tiene menos reintentos que la reserva).
* Simula un fallo persistente en `procesarPago` (ej. "Fondos insuficientes") lanzando una excepción no reintentable mediante `ApplicationFailure.newNonRetryableFailure()`.
* El Workflow debe capturar el fallo de la actividad de pago y ejecutar la compensación (`cancelarReservaInventario`) de forma segura.
* Utiliza Spring Boot para inyectar servicios simulados (Beans) en las implementaciones de las Actividades.

**Asistencia de IA:**
* *Modo Chat:* "Quiero implementar un patrón Saga básico en Temporal con Java. Tengo 3 actividades: reservar, pagar y confirmar. Si pagar falla, debo cancelar la reserva. ¿Cómo estructuro el manejo de errores con try-catch en el Workflow para asegurar que la compensación se ejecute correctamente?"
* *Claude Code / Codex:* "Crea un Workflow de Temporal en Java llamado `OrderWorkflow`. Debe llamar secuencialmente a las actividades `reservarInventario` y `procesarPago`. Si `procesarPago` lanza un error, captura la excepción y ejecuta `cancelarReservaInventario`. Asegúrate de usar las anotaciones correctas de Spring Boot para registrar las actividades en el Worker."