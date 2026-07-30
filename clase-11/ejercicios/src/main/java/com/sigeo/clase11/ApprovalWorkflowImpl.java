package com.sigeo.clase11;

import io.temporal.api.enums.v1.ParentClosePolicy;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class ApprovalWorkflowImpl implements ApprovalWorkflow {

    private String decision = null;
    private String rejectionReason = null;
    private int priority = 3;
    private final Set<String> processedCommands = new HashSet<>();

    @Override
    public String processApproval(String requestId) {
        // TODO(C11-E06): Delegar evaluación técnica a child workflow (TechnicalReviewWorkflow)
        // Usar ParentClosePolicy.PARENT_CLOSE_POLICY_TERMINATE
        // String reviewResult = ...
        
        // TODO(C11-E03): Esperar decisión o timeout (30 minutos) usando Workflow.await
        // Si hay timeout, setear decision = "TIMEOUT"
        
        // TODO(C11-E08): Simular señal cercana al timer y definir política determinista
        
        throw new UnsupportedOperationException("TODO C11-E03, C11-E06, C11-E08");
    }

    @Override
    public void approve(String commandId) {
        // TODO(C11-E01): Implementar señal approve
        // TODO(C11-E05): Implementar deduplicación usando commandId
        throw new UnsupportedOperationException("TODO C11-E01, C11-E05");
    }

    @Override
    public void reject(String commandId, String reason) {
        // TODO(C11-E01): Implementar señal reject
        // TODO(C11-E05): Implementar deduplicación usando commandId
        throw new UnsupportedOperationException("TODO C11-E01, C11-E05");
    }

    @Override
    public ApprovalState getState() {
        // TODO(C11-E02): Retornar el estado actual (decision, rejectionReason, priority)
        throw new UnsupportedOperationException("TODO C11-E02");
    }

    @Override
    public void validateUpdatePriority(int newPriority) {
        // TODO(C11-E04): Validar que la prioridad esté entre 1 y 5
        // Validar que no se pueda cambiar si ya hay una decisión
        throw new UnsupportedOperationException("TODO C11-E04");
    }

    @Override
    public int updatePriority(int newPriority) {
        // TODO(C11-E04): Actualizar y retornar la nueva prioridad
        throw new UnsupportedOperationException("TODO C11-E04");
    }
}
