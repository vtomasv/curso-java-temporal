# Clase 09 — Resolución paso a paso: Primer Workflow con Temporal

Esta guía resuelve los ejercicios de la carpeta `ejercicios/` explicando cada decisión. Léela **después** de haber intentado la resolución por tu cuenta (con o sin IA).

## Paso 0. Preparar el entorno

1. Abre una terminal y arranca el servidor de desarrollo de Temporal:
   ```bash
   temporal server start-dev
   ```
2. Abre http://localhost:8233 en tu navegador. Deberías ver la Web UI de Temporal sin workflows todavía.
3. En otra terminal, sitúate en la carpeta del ejercicio:
   ```bash
   cd clase-09/ejercicios
   mvn -q test   # los tests aún no hacen nada: hay TODOs pendientes
   ```

> **Nota importante:** el test usa `TestWorkflowEnvironment`, un servidor Temporal en memoria incluido en la dependencia `temporal-testing`. Esto significa que los tests pasan **sin necesidad** del servidor local; el servidor `start-dev` se usa para la parte de exploración visual con la Web UI.

## Paso 1. Definir la interfaz del Workflow (`HelloWorldWorkflow.java`)

Un workflow en Temporal se define primero como **interfaz Java** anotada:

```java
@WorkflowInterface
public interface HelloWorldWorkflow {

    @WorkflowMethod
    String getGreeting(String name);
}
```

Explicación de las decisiones:

- `@WorkflowInterface` marca la interfaz como contrato de un workflow. Temporal genera *stubs* (proxies) a partir de ella, tanto para el cliente que lo invoca como para el worker que lo ejecuta.
- `@WorkflowMethod` señala el **punto de entrada**. Cada workflow tiene exactamente un método con esta anotación.
- Los parámetros y el retorno deben ser **serializables** (Temporal los persiste en el historial de eventos). Un `String` es el caso más simple; en proyectos reales usarás records o clases de datos.

## Paso 2. Implementar el Workflow (`HelloWorldWorkflowImpl.java`)

```java
@WorkflowImpl(taskQueues = "HelloWorldTaskQueue")
public class HelloWorldWorkflowImpl implements HelloWorldWorkflow {

    @Override
    public String getGreeting(String name) {
        return "Hola " + name + " desde Temporal!";
    }
}
```

Explicación:

- `@WorkflowImpl(taskQueues = "HelloWorldTaskQueue")` es la anotación del **starter de Spring Boot** de Temporal: registra automáticamente esta implementación en el worker que escucha la *task queue* indicada.
- La **task queue** es el canal que conecta a quien pide ejecutar el workflow (cliente) con quien lo ejecuta (worker). Si el nombre no coincide en ambos lados, el workflow queda esperando eternamente: es el error más común de principiante.
- El código de un workflow debe ser **determinista**: sin `new Date()`, sin `Random`, sin llamadas de red directas. Todo efecto secundario va en Activities (lo veremos en la clase 10).

## Paso 3. Completar el test

En `HelloWorldWorkflowTest.java`, descomenta el registro de la implementación y la aserción:

```java
worker.registerWorkflowImplementationTypes(HelloWorldWorkflowImpl.class);
...
String result = workflow.getGreeting("Mundo");
assertEquals("Hola Mundo desde Temporal!", result);
```

Explicación del arnés de pruebas:

1. `TestWorkflowEnvironment.newInstance()` levanta un servidor Temporal **en memoria** con tiempo virtual (los `Workflow.sleep` de horas se resuelven en milisegundos).
2. `testEnv.newWorker("HelloWorldTaskQueue")` crea el worker de prueba en la misma task queue.
3. `newWorkflowStub(...)` crea el proxy cliente: al llamar `getGreeting`, el test actúa como el cliente real.

Ejecuta y verifica:

```bash
mvn test
# [INFO] Tests run: 1, Failures: 0, Errors: 0 — BUILD SUCCESS
```

## Paso 4. Ejecutarlo contra el servidor real y observar la Web UI

Con `temporal server start-dev` corriendo, configura `src/main/resources/application.yml`:

```yaml
spring:
  temporal:
    connection:
      target: local
    workers-auto-discovery:
      packages:
        - com.curso.solucion09
```

Arranca la aplicación (`mvn spring-boot:run`) y dispara el workflow desde el CLI:

```bash
temporal workflow start \
  --task-queue HelloWorldTaskQueue \
  --type HelloWorldWorkflow \
  --input '"Estudiante"' \
  --workflow-id hola-01
```

Ahora abre http://localhost:8233 y localiza el workflow `hola-01`. Observa: el **Event History** (WorkflowExecutionStarted → WorkflowTaskCompleted → WorkflowExecutionCompleted), la task queue, la entrada y el resultado. Esta trazabilidad completa de cada ejecución es la esencia de la *durable execution*.

## Errores frecuentes y cómo diagnosticarlos

| Síntoma | Causa | Solución |
|---|---|---|
| El workflow queda "Running" para siempre | No hay worker en esa task queue (nombre distinto o app caída) | Verificar el nombre exacto de la queue en ambos lados |
| `Workflow implementation doesn't implement any interface annotated with @WorkflowInterface` | Falta `@WorkflowInterface` o la clase no implementa la interfaz | Revisar anotaciones |
| El test se cuelga | Se registró la interfaz en vez de la implementación | `registerWorkflowImplementationTypes(...Impl.class)` |
| `Connection refused: localhost:7233` al ejecutar la app | Servidor Temporal no iniciado | `temporal server start-dev` |

## Cómo pudo ayudarte la IA en este ejercicio

Un prompt eficaz de depuración para este contexto habría sido:

> "Tengo un workflow de Temporal en Java con Spring Boot (starter 1.30.x). Lo inicio con `temporal workflow start` y queda en Running sin terminar. Esta es mi interfaz, mi implementación y mi application.yml: [pegar]. Dame las 3 causas más probables ordenadas y cómo verificar cada una."

Observa que el prompt: da el stack exacto y versiones, incluye el código relevante, y pide diagnóstico ordenado en lugar de un fix ciego.
