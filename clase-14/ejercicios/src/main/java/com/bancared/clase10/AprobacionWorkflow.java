package com.bancared.clase10;
import io.temporal.workflow.*;
import static com.bancared.clase10.Modelos.*;
@WorkflowInterface public interface AprobacionWorkflow {
    @WorkflowMethod ResultadoWorkflow iniciar(SolicitudTransferencia solicitud,int plazoSegundos);
    @SignalMethod void decidir(String clave,String decision);
    @QueryMethod String estado();
    @UpdateMethod long cambiarMonto(String clave,long nuevo);
    @UpdateValidatorMethod(updateName="cambiarMonto") void validarCambio(String clave,long nuevo);
}
