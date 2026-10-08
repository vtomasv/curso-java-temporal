package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import io.temporal.workflow.Workflow;
public class ReversaWorkflowImpl implements ReversaWorkflow {
    public static boolean disponible(){return false;}
    private final BankActivities registro=Workflow.newActivityStub(BankActivities.class,PoliticaActivities.persistencia());
    @Override public ResultadoWorkflow reversar(String id){
        // TODO(C10-E03): habilitar disponible() y orquestar una reversa independiente.
        // 1. Leer la transferencia mediante Activity y exigir COMPLETADA, no liquidada.
        // 2. Debitar al receptor con clave id+":reversa-debito" y operación id+"-reversa".
        // 3. Acreditar al emisor con clave id+":reversa-credito".
        // 4. Consultar comandos cuyo resultado sea incierto antes de compensar.
        // 5. Registrar COMPLETADA, RECHAZADA o PENDIENTE_REVISION, conservando el original.
        return registro.registrarReversa(id,"NO_IMPLEMENTADA","Completa C10-E03 para habilitar la reversa.");
    }
}
