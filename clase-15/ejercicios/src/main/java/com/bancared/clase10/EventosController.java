package com.bancared.clase10;
import io.temporal.client.*;
import java.util.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import static com.bancared.clase10.EventosModelos.*;
@RestController @ConditionalOnProperty(name="banking.role",havingValue="portal")
public class EventosController {
    private final OutboxStore outbox;private final InboxStore inbox;private final TransporteMensajes transporte;private final WorkflowClient client;private final TransferStore transfers;
    public EventosController(OutboxStore o,InboxStore i,TransporteMensajes t,WorkflowClient c,TransferStore ts){outbox=o;inbox=i;transporte=t;client=c;transfers=ts;}
    @GetMapping("/api/lab/eventos") public Map<String,Object> vista(){return Map.of("outbox",outbox.vista(),"inbox",inbox.todas(),"transporte",transporte.modo(),"cola",transporte.vista());}
    @GetMapping("/api/notificaciones") public List<Notificacion> notificaciones(Authentication a){boolean docente=a.getAuthorities().stream().anyMatch(r->r.getAuthority().equals("ROLE_DOCENTE"));String cuenta="bruno".equals(a.getName())?"B001":"A001";var refs=transfers.todas().stream().filter(t->docente||cuenta.equals(t.cuentaOrigen())||cuenta.equals(t.cuentaDestino())).map(Modelos.Transferencia::id).toList();return inbox.todas().stream().filter(n->docente||refs.contains(n.referencia())).toList();}
    public record Accion(String clave,String accion){}
    @PostMapping("/api/lab/eventos") public Map<String,String> ejecutar(@RequestBody Accion a){if(a.clave()==null||!a.clave().matches("[A-Za-z0-9_-]{1,40}")||!Set.of("publicar","consumir","veneno","duplicar").contains(a.accion()))throw new ErrorNegocio("VALIDACION","Clave o acción inválida.");if("veneno".equals(a.accion())){transporte.veneno(a.clave());return Map.of("estado","VENENO_ENCOLADO");}if("duplicar".equals(a.accion())){if(inbox.todas().isEmpty())throw new ErrorNegocio("VALIDACION","Primero consume una notificación.");var id=inbox.todas().getFirst().id();var e=outbox.vista().stream().filter(x->id.equals(x.get("ID"))).findFirst().orElseThrow();transporte.publicar(new Evento(id,(String)e.get("TIPO"),(String)e.get("REFERENCIA"),((Number)e.get("MONTO")).longValue(),((Number)e.get("VERSION")).intValue()));return Map.of("estado","DUPLICADO_ENCOLADO");}var w=client.newWorkflowStub(EventosWorkflow.class,WorkflowOptions.newBuilder().setTaskQueue(PortalConfiguration.TASK_QUEUE).setWorkflowId("eventos-"+a.clave()).build());WorkflowClient.start(w::procesar,a.accion());return Map.of("workflowId","eventos-"+a.clave());}
}
