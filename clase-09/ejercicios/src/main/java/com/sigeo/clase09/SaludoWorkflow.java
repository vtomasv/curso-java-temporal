package com.sigeo.clase09;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface SaludoWorkflow {

    // TODO(C09-E02): Definir el método del workflow con @WorkflowMethod
    // El método debe llamarse 'saludar' y recibir un String 'nombre', devolviendo un String.
    String saludar(String nombre);
}
