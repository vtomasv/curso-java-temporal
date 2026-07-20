# Clase 09: Introducción a Temporal.io y Ejecución Duradera

## Objetivos de la sesión
- Comprender los conceptos fundamentales de Temporal.io y el paradigma de Ejecución Duradera (Durable Execution).
- Entender la arquitectura de Temporal: Servidor, Workers, Workflows y Activities.
- Aprender las reglas estrictas del determinismo en los Workflows de Temporal.
- Instalar y utilizar Temporal CLI para interactuar con el servidor local y visualizar ejecuciones.
- Desarrollar un primer Workflow y Activity en Java utilizando el SDK oficial de Temporal.

## Cronograma propuesto
- **Hora 1:** Introducción a la Ejecución Duradera y Arquitectura de Temporal.io. Conceptos clave: Workflow, Activity, Worker y Task Queue.
- **Hora 2:** Configuración del entorno de desarrollo. Instalación de Temporal CLI, ejecución del servidor local y exploración de la interfaz web (Temporal UI).
- **Hora 3:** Desarrollo del primer Workflow y Activity en Java. Reglas de determinismo, inyección de dependencias y manejo de estado.
- **Hora 4:** Ejercicios prácticos, pruebas de resiliencia (simulación de caídas del worker durante la ejecución) y resolución de dudas.

## Ejercicios prácticos

### Ejercicio 1: Guiado - Hola Mundo con Temporal en Java
**Descripción:** En este ejercicio paso a paso, configuraremos un proyecto Java con el SDK de Temporal, crearemos un Workflow simple que salude a un usuario y un Worker que lo ejecute.

**Pasos:**
1. Inicia el servidor local de Temporal usando la terminal: `temporal server start-dev`.
2. Crea un proyecto Maven o Gradle y añade la dependencia `io.temporal:temporal-sdk`.
3. Define una interfaz para la Activity (ej. `GreetingActivities`) con un método `composeGreeting(String name)`.
4. Implementa la Activity retornando un saludo formateado.
5. Define una interfaz para el Workflow (ej. `GreetingWorkflow`) anotada con `@WorkflowInterface` y un método principal con `@WorkflowMethod`.
6. Implementa el Workflow llamando a la Activity mediante un stub (`Workflow.newActivityStub`).
7. Crea una clase `Worker` que registre el Workflow y la Activity, y comience a escuchar en una Task Queue específica.
8. Crea una clase `Starter` para iniciar la ejecución del Workflow desde el cliente y observar el resultado.

**Asistencia de IA:**
- *Modo Chat (ChatGPT/Claude):* "Actúa como un experto en Temporal.io. Dame el código paso a paso para crear un 'Hola Mundo' en Java usando el SDK de Temporal. Incluye las dependencias de Maven, la Activity, el Workflow, el Worker y el Starter."
- *Claude Code / Cursor:* "Configura un proyecto básico de Temporal en Java en este directorio. Crea las interfaces e implementaciones para un Workflow de saludo simple y un script para ejecutar el Worker."

### Ejercicio 2: Semi-guiado - Workflow de Procesamiento de Pedidos
**Descripción:** Crea un Workflow que simule el procesamiento de un pedido de e-commerce. Debe orquestar al menos tres Activities secuenciales: `verificarInventario`, `procesarPago` y `enviarConfirmacion`.

**Pistas:**
- Recuerda que las Activities pueden fallar. Temporal reintentará automáticamente las Activities fallidas según su política de reintentos.
- Usa `ActivityOptions` dentro del Workflow para configurar los timeouts (ej. `setStartToCloseTimeout`).
- Simula un fallo aleatorio en `procesarPago` lanzando una `RuntimeException` para observar cómo Temporal realiza los reintentos automáticos y cómo se refleja en la Temporal UI.

**Asistencia de IA:**
- *Modo Chat:* "Estoy creando un Workflow de procesamiento de pedidos en Temporal con Java. ¿Cómo configuro las `ActivityOptions` para que la actividad de pago tenga un timeout de 5 segundos y un máximo de 3 reintentos?"
- *Claude Code / Cursor:* "Revisa mi clase `OrderWorkflowImpl`. Añade la configuración necesaria para que las llamadas a las actividades tengan políticas de reintento personalizadas y timeouts adecuados."

### Ejercicio 3: Desafío - Workflow con Espera (Sleep) y Señales (Signals)
**Descripción:** Diseña un Workflow para una campaña de marketing por correo electrónico. El Workflow debe enviar un correo de bienvenida, esperar 3 días de forma duradera y luego enviar un correo de seguimiento. Además, el Workflow debe poder recibir una "Señal" (Signal) en cualquier momento para cancelar la campaña (por ejemplo, si el usuario se da de baja), lo que debería terminar el Workflow inmediatamente sin enviar más correos.

**Requisitos:**
- Uso de `Workflow.sleep(Duration)` para la espera duradera (no uses `Thread.sleep`).
- Definición de un método anotado con `@SignalMethod` en la interfaz del Workflow para recibir la notificación de cancelación.
- Manejo del estado interno del Workflow (una variable booleana) para saber si la campaña fue cancelada y usar `Workflow.await()` o condicionales tras el sleep.

**Asistencia de IA:**
- *Modo Chat:* "Necesito diseñar un Workflow en Temporal (Java) que espere varios días entre tareas usando `Workflow.sleep`, pero que pueda ser interrumpido si recibe una señal externa de 'cancelación' mediante un `@SignalMethod`. ¿Cuál es el patrón correcto para implementar esto respetando el determinismo?"
- *Claude Code / Cursor:* "Implementa un `MarketingCampaignWorkflow` que envíe un email, haga un `Workflow.sleep` de 3 días, y envíe otro email. Añade un `@SignalMethod` para cancelar la campaña. Asegúrate de que la espera se interrumpa o se evalúe la condición de cancelación correctamente antes de enviar el segundo correo."