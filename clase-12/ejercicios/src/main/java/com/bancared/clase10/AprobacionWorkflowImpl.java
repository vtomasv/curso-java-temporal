package com.bancared.clase10;
import io.temporal.workflow.*;
import java.time.Duration;
import java.util.*;
import static com.bancared.clase10.Modelos.*;
public class AprobacionWorkflowImpl implements AprobacionWorkflow {
    private final AprobacionActivities registro=Workflow.newActivityStub(AprobacionActivities.class,PoliticaActivities.persistencia());
    private String estado="INICIANDO",decision;private long monto,vence;private boolean iniciado;
    private final Map<String,Long> cambios=new HashMap<>();private final Set<String> decisiones=new HashSet<>();
    public String estado(){return estado+" · monto "+monto;}
    public void decidir(String clave,String valor){if(clave!=null&&clave.matches("[A-Za-z0-9_-]{1,40}")&&valor!=null&&Set.of("APROBAR","RECHAZAR","CANCELAR").contains(valor)&&decision==null&&(!iniciado||Workflow.currentTimeMillis()<vence)&&decisiones.add(clave))decision=valor;}
    public void validarCambio(String clave,long nuevo){if(!iniciado||decision!=null||Workflow.currentTimeMillis()>=vence)throw new IllegalStateException("La ventana de aprobación está cerrada.");if(clave==null||!clave.matches("[A-Za-z0-9_-]{1,40}"))throw new IllegalArgumentException("Clave inválida.");PoliticaAprobacion.validar(nuevo,30);}
    public long cambiarMonto(String clave,long nuevo){monto=CambiosAprobacion.aplicar(clave,nuevo,monto,cambios);return monto;}
    public ResultadoWorkflow iniciar(SolicitudTransferencia solicitud,int plazoSegundos){
        PoliticaAprobacion.validar(solicitud.monto(),plazoSegundos);monto=solicitud.monto();vence=Workflow.currentTimeMillis()+plazoSegundos*1000L;iniciado=true;estado="ESPERANDO";
        registro.estadoSolicitud(solicitud.commandId(),estado,"Sin movimientos hasta aprobar");
        // TODO(C12-E02): await con timeout; vencer/rechazar/cancelar sin mover dinero. Aprobar crea registro por Activity e inicia TransferenciaWorkflow hijo con ID transferencia-tx-<clave>.
        return registro.estadoSolicitud(solicitud.commandId(),"LAB_PENDIENTE","Completa C12-E02");
    }
}
