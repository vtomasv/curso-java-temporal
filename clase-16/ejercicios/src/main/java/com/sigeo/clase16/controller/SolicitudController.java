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
        // TODO(C16-E08): Implementar el endpoint POST
        // 1. Crear y guardar una nueva Solicitud en la BD con estado "CREADO"
        // 2. Iniciar el workflow SolicitudWorkflow
        // 3. Actualizar la solicitud con el workflowId
        // 4. Retornar el ID de la solicitud y el workflowId
        throw new UnsupportedOperationException("TODO C16-E08: Implementar iniciarSolicitud");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> getSolicitud(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/workflow-estado")
    public ResponseEntity<Map<String, String>> getWorkflowEstado(@PathVariable Long id) {
        // TODO(C16-E08): Implementar consulta de estado al workflow
        // 1. Buscar la solicitud por ID
        // 2. Obtener el stub del workflow usando el workflowId
        // 3. Llamar a getEstadoActual()
        // 4. Retornar el estado
        throw new UnsupportedOperationException("TODO C16-E08: Implementar getWorkflowEstado");
    }
}
