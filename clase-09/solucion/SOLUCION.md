# Solución Clase 09: Temporal.io: arquitectura y ejecución duradera

## C09-E01 — Temporal local

**Por qué:** Para desarrollar con Temporal necesitamos un servidor local. El CLI provee un servidor de desarrollo en memoria que es rápido y fácil de usar.

**Comandos:**
```bash
temporal server start-dev
```
El servidor estará accesible en `localhost:7233` y la UI en `http://localhost:8233`.
El namespace por defecto es `default`.

## C09-E02 — Saludo duradero

**Por qué:** Un Workflow en Temporal se define mediante una interfaz anotada con `@WorkflowInterface` y un método principal anotado con `@WorkflowMethod`. El Worker es el proceso que aloja la ejecución del Workflow.

**SaludoWorkflow.java:**
```java
package com.sigeo.clase09;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface SaludoWorkflow {
    @WorkflowMethod
    String saludar(String nombre);
}
```

**SaludoWorkflowImpl.java (Parte 1):**
```java
package com.sigeo.clase09;

public class SaludoWorkflowImpl implements SaludoWorkflow {
    @Override
    public String saludar(String nombre) {
        return "Hola, " + nombre;
    }
}
```

**SaludoWorker.java:**
```java
package com.sigeo.clase09;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class SaludoWorker {
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);
        
        Worker worker = factory.newWorker("SALUDO_TASK_QUEUE");
        worker.registerWorkflowImplementationTypes(SaludoWorkflowImpl.class);
        
        factory.start();
    }
}
```

## C09-E03 — Registrar auditoría

**Por qué:** Los Workflows deben ser deterministas y no pueden realizar I/O directamente. Para interactuar con el mundo exterior (como escribir en una base de datos o llamar a una API), usamos Activities.

**AuditoriaActivity.java:**
```java
package com.sigeo.clase09;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface AuditoriaActivity {
    @ActivityMethod
    void registrarAuditoria(String mensaje);
}
```

**SaludoWorkflowImpl.java (Completo):**
```java
package com.sigeo.clase09;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class SaludoWorkflowImpl implements SaludoWorkflow {

    private final AuditoriaActivity auditoriaActivity = Workflow.newActivityStub(
            AuditoriaActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    .build()
    );

    @Override
    public String saludar(String nombre) {
        auditoriaActivity.registrarAuditoria("Se saludó a: " + nombre);
        return "Hola, " + nombre;
    }
}
```

## C09-E04 — Espera de revisión

**Por qué:** `Thread.sleep` bloquea el hilo y no es duradero. Si el Worker se reinicia, el estado se pierde. `Workflow.sleep` registra un Timer en Temporal Service, liberando el hilo y permitiendo que el Workflow se reanude exactamente donde se quedó, incluso si el Worker se reinicia.

**RevisionWorkflow.java:**
```java
package com.sigeo.clase09;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface RevisionWorkflow {
    @WorkflowMethod
    String iniciarRevision(int diasEspera);
}
```

**RevisionWorkflowImpl.java:**
```java
package com.sigeo.clase09;

import io.temporal.workflow.Workflow;
import java.time.Duration;

public class RevisionWorkflowImpl implements RevisionWorkflow {
    @Override
    public String iniciarRevision(int diasEspera) {
        Workflow.sleep(Duration.ofDays(diasEspera));
        return "Revisión completada después de " + diasEspera + " días";
    }
}
```

## C09-E05 — Reinicio controlado

**Por qué:** Demuestra la durabilidad de Temporal. Al detener el Worker, el Workflow no falla, simplemente se queda esperando a que un Worker esté disponible para procesar la siguiente tarea (en este caso, cuando el Timer expire).

**ReinicioWorker.java:**
```java
package com.sigeo.clase09;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class ReinicioWorker {
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);
        
        Worker worker = factory.newWorker("REVISION_TASK_QUEUE");
        worker.registerWorkflowImplementationTypes(RevisionWorkflowImpl.class);
        
        factory.start();
    }
}
```

## C09-E06 — Aprobación v0

**Por qué:** Combina Workflows, Activities y Signals. `Workflow.await` permite pausar la ejecución hasta que una condición sea verdadera o se alcance un timeout, ideal para esperar interacciones humanas.

**AprobacionWorkflow.java:**
```java
package com.sigeo.clase09;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface AprobacionWorkflow {
    @WorkflowMethod
    String solicitarAprobacion(String idSolicitud);

    @SignalMethod
    void recibirDecision(boolean aprobado);
}
```

**AprobacionActivity.java:**
```java
package com.sigeo.clase09;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface AprobacionActivity {
    @ActivityMethod
    void notificarResultado(String idSolicitud, String resultado);
}
```

**AprobacionWorkflowImpl.java:**
```java
package com.sigeo.clase09;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class AprobacionWorkflowImpl implements AprobacionWorkflow {

    private final AprobacionActivity activity = Workflow.newActivityStub(
            AprobacionActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    .build()
    );
    
    private Boolean decision = null;

    @Override
    public String solicitarAprobacion(String idSolicitud) {
        // Esperar hasta 7 días por una decisión
        Workflow.await(Duration.ofDays(7), () -> decision != null);
        
        String resultado;
        if (decision == null) {
            resultado = "VENCIDA";
        } else if (decision) {
            resultado = "APROBADA";
        } else {
            resultado = "RECHAZADA";
        }
        
        activity.notificarResultado(idSolicitud, resultado);
        return resultado;
    }

    @Override
    public void recibirDecision(boolean aprobado) {
        this.decision = aprobado;
    }
}
```

## C09-E07 — Detectar no determinismo

**Por qué:** Los Workflows deben ser deterministas para que el replay funcione correctamente. Si el código produce resultados diferentes en cada ejecución, Temporal lanzará un `NonDeterministicWorkflowError`.

**Correcciones en NoDeterministaWorkflowImpl.java:**
```java
package com.sigeo.clase09;

import io.temporal.workflow.Workflow;
import java.time.Instant;
import java.util.UUID;

public class DeterministaWorkflowImpl {

    public String ejecutarProceso() {
        // CORRECCIÓN 1: Usar Workflow.randomUUID()
        String id = Workflow.randomUUID().toString();
        
        // CORRECCIÓN 2: Usar Workflow.currentTimeMillis()
        long inicio = Workflow.currentTimeMillis();
        
        // CORRECCIÓN 3: Usar Workflow.sleep()
        Workflow.sleep(1000);
        
        // CORRECCIÓN 4: Usar Workflow.newRandom()
        double random = Workflow.newRandom().nextDouble();
        
        // CORRECCIÓN 5: Usar el logger de Temporal
        Workflow.getLogger(DeterministaWorkflowImpl.class).info("Proceso ejecutado: " + id);
        
        return "Completado";
    }
}
```

## C09-E08 — Leer la historia

**Por qué:** Comprender el Event History es fundamental para depurar Workflows en Temporal.

**Respuestas:**
1. `WorkflowTaskScheduled` indica que el Worker debe ejecutar lógica del Workflow (avanzar el estado). `ActivityTaskScheduled` indica que el Worker debe ejecutar el código de una Activity (efectos secundarios).
2. Durante el replay, el código del Workflow se vuelve a ejecutar desde el principio, pero las Activities y Timers NO se vuelven a ejecutar. En su lugar, Temporal inyecta los resultados previamente registrados en el Event History.
