package com.sigeo.clase10.e06;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ExportWorkflow {
    @WorkflowMethod
    void startExport();
}
