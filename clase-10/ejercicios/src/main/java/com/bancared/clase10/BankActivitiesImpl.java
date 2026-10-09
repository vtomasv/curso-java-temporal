package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import io.temporal.activity.Activity;
import io.temporal.failure.ApplicationFailure;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestClientException;

public class BankActivitiesImpl implements BankActivities {
    private final BankGateway gateway; private final TransferStore store;
    public BankActivitiesImpl(BankGateway gateway,TransferStore store){this.gateway=gateway;this.store=store;}
    @Override public ReciboBanco ejecutar(String banco,ComandoBanco c){
        int intento=Activity.getExecutionContext().getInfo().getAttempt();
        try{ReciboBanco r=gateway.ejecutar(banco,c);store.intento(c.transferId(),c.direccion().name(),intento,"APLICADO");return r;}
        catch(RestClientResponseException e){
            store.intento(c.transferId(),c.direccion().name(),intento,"HTTP_"+e.getStatusCode().value());
            if(e.getStatusCode().is4xxClientError())throw ApplicationFailure.newNonRetryableFailure("El banco rechazó el comando (HTTP "+e.getStatusCode().value()+").","RECHAZO_BANCO");
            throw ApplicationFailure.newFailure("El banco no respondió correctamente.","BANCO_NO_DISPONIBLE");
        }catch(RestClientException e){store.intento(c.transferId(),c.direccion().name(),intento,"SIN_RESPUESTA");throw ApplicationFailure.newFailure("No hay confirmación del banco.","BANCO_NO_DISPONIBLE");}
    }
    @Override public ConsultaComando consultar(String banco,String id){
        try{return gateway.consultar(banco,id);}catch(RestClientException e){throw ApplicationFailure.newFailure("No se pudo confirmar el estado del comando.","BANCO_NO_DISPONIBLE");}
    }
    @Override public Transferencia transferencia(String id){return store.obtener(id);}
    @Override public ResultadoWorkflow terminar(String id,String estado,String detalle){return store.terminar(id,estado,detalle);}
    @Override public ResultadoWorkflow registrarReversa(String id,String estado,String detalle){return store.reversa(id,estado,detalle);}
}
