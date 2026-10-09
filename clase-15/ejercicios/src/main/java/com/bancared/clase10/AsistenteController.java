package com.bancared.clase10;
import io.temporal.client.*;
import java.util.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import static com.bancared.clase10.AsistenteModelos.*;
@RestController @ConditionalOnProperty(name="banking.role",havingValue="portal")
public class AsistenteController {
    private final AsistenteStore store;private final WorkflowClient client;private final TransferStore transfers;
    public AsistenteController(AsistenteStore s,WorkflowClient c,TransferStore t){store=s;client=c;transfers=t;}
    private boolean docente(Authentication a){return a.getAuthorities().stream().anyMatch(r->r.getAuthority().equals("ROLE_DOCENTE"));}
    @GetMapping("/api/asistente") public Map<String,Object> vista(Authentication a){return Map.of("consultas",store.consultas(a.getName(),docente(a)),"documentos",BaseConocimiento.documentos());}
    @PostMapping("/api/asistente") public Map<String,String> consultar(@RequestBody Consulta c,Authentication a){store.iniciar(c,a.getName());var w=client.newWorkflowStub(AsistenteWorkflow.class,WorkflowOptions.newBuilder().setTaskQueue(PortalConfiguration.TASK_QUEUE).setWorkflowId("asistente-"+c.clave()).build());WorkflowClient.start(w::responder,c);return Map.of("workflowId","asistente-"+c.clave());}
    @GetMapping("/api/lab/operacion") public Resumen operacion(){return ResumenOperativo.calcular(transfers.todas());}
}
