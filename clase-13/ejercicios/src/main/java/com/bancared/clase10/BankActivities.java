package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import io.temporal.activity.ActivityInterface;
@ActivityInterface
public interface BankActivities {
    ReciboBanco ejecutar(String banco,ComandoBanco comando);
    ConsultaComando consultar(String banco,String commandId);
    Transferencia transferencia(String id);
    ResultadoWorkflow terminar(String id,String estado,String detalle);
    ResultadoWorkflow registrarReversa(String id,String estado,String detalle);
}
