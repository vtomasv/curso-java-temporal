package com.curso.ejercicio09;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

/**
 * TODO(ejercicio-1): 
 * 1. Define la interfaz del Workflow con @WorkflowInterface
 * 2. Agrega el método principal con @WorkflowMethod que reciba un String (nombre) y devuelva un String
 */
@WorkflowInterface
public interface HelloWorldWorkflow {

    @WorkflowMethod
    String getGreeting(String name);
}
