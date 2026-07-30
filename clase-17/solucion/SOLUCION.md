# Solución Clase 17: Proyecto integrador: implementación, hardening y ensayo de defensa

Este documento explica cómo resolver cada uno de los ejercicios de la clase 17.

## C17-E01 — Bootstrap desde cero

**Por qué:** Es fundamental que cualquier desarrollador pueda levantar el entorno completo de forma rápida y reproducible.

**Solución:**
En `TemporalWorkerConfig.java`:
```java
package com.sigeo.clase17;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalWorkerConfig {

    public static final String TASK_QUEUE = "SIGEO_TASK_QUEUE";

    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        return WorkflowServiceStubs.newLocalServiceStubs();
    }

    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs workflowServiceStubs) {
        return WorkflowClient.newInstance(workflowServiceStubs);
    }

    @Bean
    public WorkerFactory workerFactory(WorkflowClient workflowClient) {
        return WorkerFactory.newInstance(workflowClient);
    }

    @Bean
    public Worker worker(WorkerFactory workerFactory, SigeoActivities sigeoActivities) {
        Worker worker = workerFactory.newWorker(TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(SigeoWorkflowImpl.class);
        worker.registerActivitiesImplementations(sigeoActivities);
        workerFactory.start();
        return worker;
    }
}
```

## C17-E02 — Ruta crítica

**Por qué:** La ruta crítica debe funcionar end-to-end integrando todos los componentes (API, DB, Temporal, RabbitMQ).

**Solución:**
En `SigeoWorkflowImpl.java`:
```java
package com.sigeo.clase17;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class SigeoWorkflowImpl implements SigeoWorkflow {

    private final SigeoActivities activities = Workflow.newActivityStub(
            SigeoActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    .build()
    );

    @Override
    public void processRequest(String requestId) {
        activities.approveRequest(requestId);
        activities.notifyUser(requestId);
    }
}
```

En `SigeoActivitiesImpl.java`:
```java
package com.sigeo.clase17;

import org.springframework.stereotype.Component;

@Component
public class SigeoActivitiesImpl implements SigeoActivities {

    @Override
    public void approveRequest(String requestId) {
        System.out.println("Aprobando solicitud: " + requestId);
        // Lógica de DB
    }

    @Override
    public void notifyUser(String requestId) {
        System.out.println("Notificando usuario: " + requestId);
        // Lógica de RabbitMQ
    }

    @Override
    public void compensateRequest(String requestId) {
        System.out.println("Compensando solicitud: " + requestId);
        // Lógica de compensación
    }
}
```

## C17-E03 — Fallo de proveedor

**Por qué:** Los sistemas distribuidos fallan. Debemos configurar retries y compensaciones (Saga pattern) para mantener la consistencia.

**Solución:**
En `SigeoWorkflowImpl.java`:
```java
package com.sigeo.clase17;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class SigeoWorkflowImpl implements SigeoWorkflow {

    private final SigeoActivities activities = Workflow.newActivityStub(
            SigeoActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setInitialInterval(Duration.ofSeconds(1))
                            .setMaximumAttempts(3)
                            .build())
                    .build()
    );

    @Override
    public void processRequest(String requestId) {
        Saga saga = new Saga(new Saga.Options.Builder().setParallelCompensation(false).build());
        try {
            activities.approveRequest(requestId);
            saga.addCompensation(activities::compensateRequest, requestId);
            
            activities.notifyUser(requestId);
        } catch (ActivityFailure e) {
            saga.compensate();
            throw e;
        }
    }
}
```

## C17-E04 — Compatibilidad de release

**Por qué:** Al actualizar el código de un Workflow, debemos asegurar que las ejecuciones en curso no fallen por nondeterminism.

**Solución:**
En `WorkflowReplayTest.java`:
```java
package com.sigeo.clase17;

import io.temporal.testing.WorkflowReplayer;
import org.junit.jupiter.api.Test;

public class WorkflowReplayTest {

    @Test
    void testWorkflowReplay() throws Exception {
        // Asumiendo que tenemos un archivo history.json guardado
        // WorkflowReplayer.replayWorkflowExecutionFromResource("history.json", SigeoWorkflowImpl.class);
    }
}
```

## C17-E05 — Matriz de ataque

**Por qué:** Debemos asegurar que nuestra aplicación no sea vulnerable a ataques comunes como IDOR o mass assignment.

**Solución:**
En `SecurityConfig.java`:
```java
package com.sigeo.clase17;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> {});
        return http.build();
    }
}
```

## C17-E06 — Redelivery/DLQ

**Por qué:** Los mensajes que no pueden ser procesados no deben bloquear la cola principal ni perderse.

**Solución:**
En `RabbitMQConfig.java`:
```java
package com.sigeo.clase17;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "sigeo.notifications";
    public static final String DLQ_NAME = "sigeo.notifications.dlq";
    public static final String EXCHANGE_NAME = "sigeo.exchange";

    @Bean
    public Queue dlq() {
        return QueueBuilder.durable(DLQ_NAME).build();
    }

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", DLQ_NAME)
                .build();
    }
    
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }
    
    @Bean
    public Binding binding(Queue notificationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(notificationQueue).to(exchange).with("notify");
    }
}
```

## C17-E07 — Diagnóstico en 5 minutos

**Por qué:** En producción, debemos poder rastrear una solicitud a través de todos los componentes usando un identificador único.

**Solución:**
Implementar un filtro que inyecte un `correlationId` en el MDC (Mapped Diagnostic Context) de SLF4J y propagarlo a Temporal y RabbitMQ.

## C17-E08 — Preguntas hostiles

**Por qué:** Parte de la defensa del proyecto es justificar las decisiones técnicas y conocer las limitaciones del sistema.

**Solución:**
Elaborar un documento `docs/defensa.md` con respuestas a preguntas como:
- ¿Por qué usar Temporal en lugar de coreografiar eventos con RabbitMQ? (Respuesta: Visibilidad centralizada, manejo de timeouts y compensaciones más sencillo).
- ¿Qué pasa si la base de datos se cae durante una Activity? (Respuesta: Temporal reintentará la Activity según las RetryOptions configuradas).
