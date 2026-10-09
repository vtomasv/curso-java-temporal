package com.bancared.clase10;
import org.springframework.context.annotation.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.*;
import io.temporal.failure.ApplicationFailure;
import static com.bancared.clase10.LiquidacionModelos.*;
@Configuration @ConditionalOnProperty(name="banking.role",havingValue="portal")
public class LiquidacionConfiguration {
    @Bean CompensacionStore compensacionStore(JdbcTemplate j,TransferStore t){return new CompensacionStore(j,t);}
    @Bean ExtraWorkerModule liquidacionModule(CompensacionStore s,BankGateway b){return w->{w.registerWorkflowImplementationTypes(LiquidacionWorkflowImpl.class);w.registerActivitiesImplementations(new LiquidacionActivities(){
        public Lote prepararLote(String id){return s.preparar(id);}public Lote terminarLote(String id,String e,String d){return s.terminar(id,e,d);}
        public ReciboLiquidez moverLiquidez(String banco,OrdenLiquidez c){try{return b.moverLiquidez(banco,c);}catch(RestClientResponseException e){if(e.getStatusCode().is4xxClientError())throw ApplicationFailure.newNonRetryableFailure("Banco rechazó liquidez","RECHAZO_BANCO");throw ApplicationFailure.newFailure("Liquidez sin confirmación","BANCO_NO_DISPONIBLE");}catch(RestClientException e){throw ApplicationFailure.newFailure("Liquidez sin confirmación","BANCO_NO_DISPONIBLE");}}
        public boolean confirmarLiquidez(String banco,String clave){return b.confirmarLiquidez(banco,clave);}
    });};}
}
