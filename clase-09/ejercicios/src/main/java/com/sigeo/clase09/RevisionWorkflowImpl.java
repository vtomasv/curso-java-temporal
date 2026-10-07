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
