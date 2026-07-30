package com.sigeo.clase09;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface RevisionWorkflow {

    // TODO(C09-E04): Definir el método del workflow
    // El método debe llamarse 'iniciarRevision' y recibir un int 'diasEspera', devolviendo un String.
    String iniciarRevision(int diasEspera);
}
