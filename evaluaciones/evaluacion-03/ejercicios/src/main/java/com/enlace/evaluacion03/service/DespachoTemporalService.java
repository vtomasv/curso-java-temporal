package com.enlace.evaluacion03.service;

import com.enlace.evaluacion03.config.TemporalConfig;
import com.enlace.evaluacion03.workflow.DespachoInput;
import com.enlace.evaluacion03.workflow.DespachoWorkflow;
import com.enlace.evaluacion03.workflow.ProgresoDespacho;
import com.enlace.evaluacion03.workflow.ReciboEnvio;
import io.temporal.api.enums.v1.WorkflowIdReusePolicy;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowExecutionAlreadyStarted;
import io.temporal.client.WorkflowOptions;
import org.springframework.stereotype.Service;

import java.util.List;

/** Puente de aplicación entre Spring MVC y el motor Temporal. */
@Service
public class DespachoTemporalService {

    private static final String WORKFLOW_PREFIX = "despacho-";

    private final WorkflowClient client;

    public DespachoTemporalService(WorkflowClient client) {
        this.client = client;
    }

    public String iniciar(DespachoInput input) {
        String workflowId = workflowId(input.ordenId());
        DespachoWorkflow workflow = client.newWorkflowStub(
                DespachoWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(workflowId)
                        .setTaskQueue(TemporalConfig.TASK_QUEUE)
                        .setWorkflowIdReusePolicy(
                                WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_REJECT_DUPLICATE)
                        .build()
        );
        try {
            WorkflowClient.start(workflow::iniciar, input);
        } catch (WorkflowExecutionAlreadyStarted ignored) {
            // El ID estable recupera la misma intención sin duplicar el despacho.
        }
        return workflowId;
    }

    public ProgresoDespacho consultarProgreso(String ordenId) {
        return workflow(ordenId).consultarProgreso();
    }

    public List<ReciboEnvio> consultarRecibos(String ordenId) {
        return List.copyOf(workflow(ordenId).consultarRecibos());
    }

    public void confirmarRecepcion(String ordenId, String unidad, String reciboId) {
        workflow(ordenId).confirmarRecepcion(
                texto(unidad, "unidad", 80),
                texto(reciboId, "reciboId", 120)
        );
    }

    public boolean cancelar(String ordenId, String motivo) {
        return workflow(ordenId).cancelar(texto(motivo, "motivo", 160));
    }

    public String workflowId(String ordenId) {
        if (ordenId == null || ordenId.isBlank()) throw new IllegalArgumentException("ordenId");
        return WORKFLOW_PREFIX + ordenId.trim();
    }

    private DespachoWorkflow workflow(String ordenId) {
        return client.newWorkflowStub(DespachoWorkflow.class, workflowId(ordenId));
    }

    private static String texto(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank() || valor.length() > maximo) {
            throw new IllegalArgumentException(campo);
        }
        return valor.trim();
    }
}
