package com.sigeo.clase12;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/sagas")
public class SagaController {

    private final WorkflowClient workflowClient;

    public SagaController(WorkflowClient workflowClient) {
        this.workflowClient = workflowClient;
    }

    @PostMapping
    public Map<String, String> startSaga(@RequestParam(defaultValue = "false") boolean failAtBudget) {
        // TODO(C12-E06): POST inicia saga.
        // Iniciar el workflow de forma asíncrona y retornar el workflowId.
        throw new UnsupportedOperationException("TODO C12-E06");
    }

    @GetMapping("/{workflowId}")
    public Map<String, String> getSagaStatus(@PathVariable String workflowId) {
        // TODO(C12-E06): GET consulta estado/resultado.
        // Obtener el estado del workflow usando el workflowId.
        throw new UnsupportedOperationException("TODO C12-E06");
    }
}
