package com.bancared.clase10;
import io.temporal.client.*;
import java.util.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import static com.bancared.clase10.Modelos.*;
@RestController @ConditionalOnProperty(name="banking.role",havingValue="portal")
public class AprobacionController {
    private final AprobacionStore store;private final WorkflowClient client;
    public AprobacionController(AprobacionStore s,WorkflowClient c){store=s;client=c;}
    private boolean docente(Authentication a){return a.getAuthorities().stream().anyMatch(r->r.getAuthority().equals("ROLE_DOCENTE"));}
    private void propietario(Authentication a,String cuenta){if(!docente(a)&&!("bruno".equals(a.getName())?"B001":"A001").equals(cuenta))throw new ErrorNegocio("NO_AUTORIZADO","Solo puedes consultar o cancelar tu solicitud.");}
    public record Solicitud(SolicitudTransferencia transferencia,int plazoSegundos){}
    @PostMapping("/api/aprobaciones") public Map<String,String> crear(@RequestBody Solicitud s,Authentication a){propietario(a,s.transferencia().cuentaOrigen());store.crear(s.transferencia(),s.plazoSegundos());var w=client.newWorkflowStub(AprobacionWorkflow.class,WorkflowOptions.newBuilder().setTaskQueue(PortalConfiguration.TASK_QUEUE).setWorkflowId("aprobacion-"+s.transferencia().commandId()).build());WorkflowClient.start(w::iniciar,s.transferencia(),s.plazoSegundos());return Map.of("workflowId","aprobacion-"+s.transferencia().commandId());}
    @GetMapping("/api/aprobaciones") public List<Map<String,Object>> estado(Authentication a){return store.todas().stream().filter(r->docente(a)||Objects.equals(r.get("CUENTA_ORIGEN"),"bruno".equals(a.getName())?"B001":"A001")).toList();}
    private AprobacionWorkflow stub(String clave){return client.newWorkflowStub(AprobacionWorkflow.class,"aprobacion-"+clave);}
    @GetMapping("/api/aprobaciones/{clave}/query") public Map<String,String> consultar(@PathVariable String clave,Authentication a){propietario(a,store.origen(clave));return Map.of("estado",stub(clave).estado());}
    public record Decision(String clave,String decision){}
    @PostMapping("/api/lab/aprobaciones/{clave}/signal") public Map<String,String> decidir(@PathVariable String clave,@RequestBody Decision d){if(!Set.of("APROBAR","RECHAZAR","CANCELAR").contains(d.decision()))throw new ErrorNegocio("VALIDACION","Decisión inválida.");stub(clave).decidir(d.clave(),d.decision());return Map.of("estado","SIGNAL_ACEPTADO_POR_SERVIDOR");}
    @PostMapping("/api/aprobaciones/{clave}/cancelar") public Map<String,String> cancelar(@PathVariable String clave,Authentication a){propietario(a,store.origen(clave));stub(clave).decidir("cancelar-"+clave,"CANCELAR");return Map.of("estado","CANCELACION_SOLICITADA");}
    public record Cambio(String clave,long monto){}
    @PostMapping("/api/lab/aprobaciones/validar") public Map<String,String> validar(@RequestBody java.util.Map<String,Long> c){PoliticaAprobacion.validar(c.getOrDefault("monto",0L),c.getOrDefault("plazo",0L).intValue());return Map.of("estado","VALIDACION_OK");}
    @PostMapping("/api/lab/aprobaciones/{clave}/update") public Map<String,Long> cambiar(@PathVariable String clave,@RequestBody Cambio c){return Map.of("monto",stub(clave).cambiarMonto(c.clave(),c.monto()));}
}
