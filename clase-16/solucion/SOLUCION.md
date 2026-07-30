# Solución Clase 16 - Proyecto Integrador

Este documento contiene las soluciones a los ejercicios de código (E08) de la clase 16. Los ejercicios E01 a E07 son de diseño y planificación, por lo que sus entregables son documentos Markdown, diagramas y tableros de issues.

## C16-E08 — Camino mínimo (Walking Skeleton)

El objetivo de este ejercicio es tener un esqueleto funcional que conecte la API REST, la base de datos y Temporal.

### 1. Implementar `SolicitudWorkflowImpl`

El workflow debe orquestar el cambio de estados y llamar a las actividades correspondientes.

```java
package com.sigeo.clase16.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class SolicitudWorkflowImpl implements SolicitudWorkflow {

    private String estado = "INICIADO";

    private final SolicitudActivities activities = Workflow.newActivityStub(
            SolicitudActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    .build()
    );

    @Override
    public void procesarSolicitud(Long solicitudId) {
        // 1. Actualizar estado a "PROCESANDO"
        this.estado = "PROCESANDO";
        
        // 2. Llamar a la actividad guardarEstadoSolicitud
        activities.guardarEstadoSolicitud(solicitudId, this.estado);
        
        // Simulamos un procesamiento
        Workflow.sleep(Duration.ofSeconds(5));
        
        // 3. Actualizar estado a "COMPLETADO"
        this.estado = "COMPLETADO";
        
        // 4. Llamar a la actividad guardarEstadoSolicitud
        activities.guardarEstadoSolicitud(solicitudId, this.estado);
    }

    @Override
    public String getEstadoActual() {
        return estado;
    }
}
```

### 2. Implementar `SolicitudActivitiesImpl`

La actividad debe interactuar con la base de datos para persistir el estado.

```java
package com.sigeo.clase16.workflow;

import com.sigeo.clase16.domain.Solicitud;
import com.sigeo.clase16.repository.SolicitudRepository;
import org.springframework.stereotype.Component;

@Component
public class SolicitudActivitiesImpl implements SolicitudActivities {

    private final SolicitudRepository repository;

    public SolicitudActivitiesImpl(SolicitudRepository repository) {
        this.repository = repository;
    }

    @Override
    public void guardarEstadoSolicitud(Long solicitudId, String estado) {
        Solicitud solicitud = repository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada: " + solicitudId));
        
        solicitud.setEstado(estado);
        repository.save(solicitud);
    }
}
```

### 3. Implementar `SolicitudController`

El controlador debe iniciar el workflow y retornar los IDs.

```java
package com.sigeo.clase16.controller;

import com.sigeo.clase16.domain.Solicitud;
import com.sigeo.clase16.repository.SolicitudRepository;
import com.sigeo.clase16.config.TemporalConfig;
import com.sigeo.clase16.workflow.SolicitudWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudRepository repository;
    private final WorkflowClient workflowClient;

    public SolicitudController(SolicitudRepository repository, WorkflowClient workflowClient) {
        this.repository = repository;
        this.workflowClient = workflowClient;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> iniciarSolicitud(@RequestBody Map<String, String> request) {
        String descripcion = request.getOrDefault("descripcion", "Sin descripción");
        
        // 1. Crear y guardar una nueva Solicitud en la BD con estado "CREADO"
        Solicitud solicitud = new Solicitud(descripcion, "CREADO");
        solicitud = repository.save(solicitud);
        
        // 2. Iniciar el workflow SolicitudWorkflow
        String workflowId = "solicitud-" + solicitud.getId() + "-" + UUID.randomUUID().toString();
        
        SolicitudWorkflow workflow = workflowClient.newWorkflowStub(
                SolicitudWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(workflowId)
                        .setTaskQueue(TemporalConfig.TASK_QUEUE)
                        .build()
        );
        
        // Ejecución asíncrona
        WorkflowClient.start(workflow::procesarSolicitud, solicitud.getId());
        
        // 3. Actualizar la solicitud con el workflowId
        solicitud.setWorkflowId(workflowId);
        repository.save(solicitud);
        
        // 4. Retornar el ID de la solicitud y el workflowId
        return ResponseEntity.ok(Map.of(
                "solicitudId", solicitud.getId().toString(),
                "workflowId", workflowId
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> getSolicitud(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/workflow-estado")
    public ResponseEntity<Map<String, String>> getWorkflowEstado(@PathVariable Long id) {
        return repository.findById(id).map(solicitud -> {
            if (solicitud.getWorkflowId() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "La solicitud no tiene un workflow asociado"));
            }
            
            SolicitudWorkflow workflow = workflowClient.newWorkflowStub(
                    SolicitudWorkflow.class,
                    solicitud.getWorkflowId()
            );
            
            String estado = workflow.getEstadoActual();
            
            return ResponseEntity.ok(Map.of(
                    "solicitudId", id.toString(),
                    "workflowId", solicitud.getWorkflowId(),
                    "estadoWorkflow", estado
            ));
        }).orElse(ResponseEntity.notFound().build());
    }
}
```
