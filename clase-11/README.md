# Clase 11: Patrones Avanzados en Temporal: Señales y Consultas

## Objetivos de la sesión
* Comprender y aplicar el uso de `@SignalMethod` para enviar datos a un Workflow en ejecución de forma asíncrona.
* Implementar `@QueryMethod` para consultar el estado interno de un Workflow sin modificar su historial ni su estado.
* Utilizar `Workflow.await` para pausar la ejecución de un Workflow hasta que se cumpla una condición específica.
* Diseñar e implementar timers duraderos (`Workflow.sleep` o timeouts en `await`) para manejar esperas prolongadas de forma resiliente.
* Integrar señales, consultas y timers en casos de uso reales, como procesos de aprobación o carritos de compras.

## Cronograma propuesto (4 horas)
* **00:00 - 00:45**: Introducción a Señales (`@SignalMethod`) y Consultas (`@QueryMethod`). Conceptos, diferencias y casos de uso.
* **00:45 - 01:30**: Ejercicio Guiado: Implementación de un Workflow interactivo básico con señales y consultas.
* **01:30 - 01:45**: Descanso.
* **01:45 - 02:30**: Uso de `Workflow.await` y Timers duraderos. Manejo de tiempos de espera, condiciones y reinicio de timers.
* **02:30 - 03:15**: Ejercicio Semi-guiado: Proceso de aprobación de documentos con límite de tiempo (timeout).
* **03:15 - 04:00**: Ejercicio Desafío: Sistema de carrito de compras abandonado con notificaciones y cierre automático.

## Ejercicios prácticos

### Ejercicio 1: Guiado - Contador Interactivo con Señales y Consultas
**Descripción:** Crear un Workflow que mantenga un contador numérico interno. El Workflow debe permitir incrementar o decrementar el contador mediante señales, y consultar el valor actual en cualquier momento mediante una consulta. El Workflow se mantendrá en ejecución hasta que reciba una señal explícita de "finalizar".

**Pasos:**
1. Define la interfaz del Workflow (`ContadorWorkflow`) con un método principal `@WorkflowMethod`, métodos para modificar el contador `@SignalMethod` (ej. `incrementar`, `decrementar`, `finalizar`), y un método para obtener el valor `@QueryMethod` (ej. `obtenerValor`).
2. Implementa la interfaz. Usa una variable local entera para el contador y un booleano `activo` para controlar el ciclo de vida del Workflow.
3. En el método principal, usa `Workflow.await(() -> !activo)` para mantener el Workflow en ejecución hasta que la variable cambie a falso (cuando se reciba la señal de finalizar).
4. Crea un iniciador (Starter) que inicie el Workflow, envíe un par de señales de incremento, consulte el valor imprimiéndolo en consola, y luego envíe la señal de finalizar.

**Asistencia de IA:**
* *Prompt para Chat:* "Actúa como un experto en Temporal con Java. Ayúdame a crear un Workflow paso a paso que tenga un contador interno. Necesito usar `@SignalMethod` para cambiar el valor y `@QueryMethod` para leerlo. Dame primero la interfaz y explícame cada anotación."
* *Prompt para Claude Code / Codex:* "Genera una interfaz de Temporal Workflow en Java llamada `ContadorWorkflow` con un `@WorkflowMethod` void iniciar(), tres `@SignalMethod` (incrementar, decrementar, finalizar) y un `@QueryMethod` int obtenerValor(). Luego genera su implementación usando `Workflow.await` para mantenerlo vivo hasta recibir la señal de finalizar."

### Ejercicio 2: Semi-guiado - Proceso de Aprobación de Documentos con Timeout
**Descripción:** Implementar un Workflow que simule la revisión de un documento importante. El Workflow debe esperar la aprobación o rechazo de un supervisor mediante una señal. Si el supervisor no responde en un tiempo determinado (ej. 2 minutos simulados), el Workflow debe auto-rechazar el documento usando un timer duradero.

**Pistas:**
* Necesitarás un `@SignalMethod` para recibir la decisión (ej. un String que sea "APROBADO" o "RECHAZADO").
* En tu método principal, puedes usar la versión de `await` que acepta un timeout: `Workflow.await(Duration.ofMinutes(2), () -> decision != null)`.
* Recuerda que `Workflow.await` con timeout devuelve `true` si la condición se cumplió (llegó la señal), o `false` si el tiempo expiró. Usa este valor de retorno booleano para saber si fue un timeout y actuar en consecuencia.
* Crea un Worker y un Starter para probar los dos escenarios: enviando la señal a tiempo, y dejando que expire el tiempo.

**Asistencia de IA:**
* *Prompt para Chat:* "Estoy haciendo un Workflow en Temporal (Java) que espera una señal de aprobación. ¿Cómo puedo usar `Workflow.await` con un timeout de 2 minutos para que, si no llega la señal, el estado cambie a 'RECHAZADO' automáticamente? Dame un ejemplo del bloque de código del método principal."
* *Prompt para Claude Code / Codex:* "Crea un Workflow de Temporal llamado `AprobacionWorkflow`. Debe tener un `@SignalMethod` para recibir un String 'decision'. En el `@WorkflowMethod`, espera hasta 2 minutos por la decisión usando `Workflow.await`. Si expira el tiempo, asigna 'TIMEOUT_RECHAZADO' a la decisión y retorna ese valor."

### Ejercicio 3: Desafío - Carrito de Compras Abandonado
**Descripción:** Diseñar e implementar un Workflow para gestionar un carrito de compras de e-commerce. El Workflow debe permitir agregar items (señal), remover items (señal), consultar los items actuales (consulta) y realizar el checkout (señal). Si el carrito permanece inactivo (sin recibir señales de agregar/remover) por más de 1 hora (puedes usar 1 minuto para facilitar las pruebas), el Workflow debe enviar un correo recordatorio (simulado mediante una Actividad) y, si pasa otra hora sin actividad, vaciar el carrito y terminar.

**Requisitos:**
* Interfaz de Workflow con múltiples `@SignalMethod` y al menos un `@QueryMethod` para devolver la lista de productos.
* Uso avanzado de `Workflow.await` y manejo de estado interno (lista de items y marca de tiempo de la última actualización).
* Integración con una Actividad (`NotificacionActivity.enviarRecordatorio()`).
* Lógica para reiniciar el timer de inactividad cada vez que se modifica el carrito (el Workflow no debe expirar si el usuario sigue agregando productos).

**Asistencia de IA:**
* *Prompt para Chat:* "Tengo un desafío complejo en Temporal con Java. Necesito modelar un carrito de compras como un Workflow. El problema es que necesito un timer de inactividad de 1 hora que se reinicie cada vez que el usuario agrega o quita un producto mediante una señal. ¿Cuál es el mejor patrón o estructura en Java para reiniciar este timer de inactividad dentro del Workflow usando `Workflow.await`?"
* *Prompt para Claude Code / Codex:* "Implementa un Workflow de Temporal en Java para un carrito de compras. Usa `@SignalMethod` para agregar/quitar items y `@QueryMethod` para ver el carrito. Implementa un bucle en el `@WorkflowMethod` que espere inactividad usando `Workflow.await` con timeout. Si el timeout ocurre, llama a una Actividad para enviar un recordatorio. Asegúrate de que el timer de inactividad se reinicie correctamente si llegan nuevas señales."