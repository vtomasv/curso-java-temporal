package com.bancared.clase10;
import io.temporal.client.*;
import java.util.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;
import static com.bancared.clase10.LiquidacionModelos.*;
@RestController @ConditionalOnProperty(name="banking.role",havingValue="portal") @RequestMapping("/api/lab/liquidacion")
public class LiquidacionController {
    private final CompensacionStore store;private final WorkflowClient client;private final BankGateway bancos;private final TransferStore transfers;
    public LiquidacionController(CompensacionStore s,WorkflowClient c,BankGateway b,TransferStore t){store=s;client=c;bancos=b;transfers=t;}
    @PostMapping("/preview") public Map<String,Long> netos(){return CalculoNeto.calcular(transfers.todas().stream().filter(t->!t.liquidada()&&t.lote()==null).toList());}
    @GetMapping public Map<String,Object> vista(){return Map.of("lotes",store.todos(),"liquidez",Map.of("CORDILLERA",bancos.liquidez("CORDILLERA"),"PACIFICO",bancos.liquidez("PACIFICO")));}
    @PostMapping("/{id}/preparar") public Lote preparar(@PathVariable String id){return store.preparar(id);}
    @PostMapping("/{id}/ejecutar") public Map<String,String> ejecutar(@PathVariable String id){store.obtener(id);var w=client.newWorkflowStub(LiquidacionWorkflow.class,WorkflowOptions.newBuilder().setTaskQueue(PortalConfiguration.TASK_QUEUE).setWorkflowId("liquidacion-"+id).build());try{WorkflowClient.start(w::liquidar,id);}catch(WorkflowExecutionAlreadyStarted e){/* Recuperar la misma operación; no reabrir lotes cerrados. */}return Map.of("workflowId","liquidacion-"+id);}
}
