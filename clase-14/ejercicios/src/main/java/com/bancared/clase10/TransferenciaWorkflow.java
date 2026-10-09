package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import io.temporal.workflow.*;
@WorkflowInterface
public interface TransferenciaWorkflow {
    @WorkflowMethod ResultadoWorkflow transferir(Transferencia t);
}
