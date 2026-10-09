package com.bancared.clase10;
import org.springframework.context.annotation.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import static com.bancared.clase10.Modelos.*;
@Configuration @ConditionalOnProperty(name="banking.role",havingValue="portal")
public class AprobacionConfiguration {
    @Bean AprobacionStore aprobacionStore(JdbcTemplate j,TransferStore t){return new AprobacionStore(j,t);}
    @Bean ExtraWorkerModule aprobacionModule(AprobacionStore s){return w->{w.registerWorkflowImplementationTypes(AprobacionWorkflowImpl.class);w.registerActivitiesImplementations(new AprobacionActivities(){public ResultadoWorkflow estadoSolicitud(String k,String e,String d){return s.estado(k,e,d);}public Transferencia prepararTransferencia(SolicitudTransferencia r){return s.preparar(r);}});};}
}
