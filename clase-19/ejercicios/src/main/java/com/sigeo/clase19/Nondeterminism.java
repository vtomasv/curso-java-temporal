package com.sigeo.clase19;

import io.temporal.workflow.Workflow;
import java.util.UUID;

public class Nondeterminism {

    public String generarId() {
        // TODO(C19-E04): Corregir el fallo de no determinismo
        return UUID.randomUUID().toString();
    }
}
