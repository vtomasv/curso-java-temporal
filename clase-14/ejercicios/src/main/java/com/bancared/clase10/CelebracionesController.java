package com.bancared.clase10;
import io.temporal.client.*;
import io.temporal.client.schedules.*;
import io.temporal.api.enums.v1.ScheduleOverlapPolicy;
import java.time.LocalDate;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController @ConditionalOnProperty(name="banking.role",havingValue="portal")
public class CelebracionesController {
    private final CelebracionesStore store;private final WorkflowClient client;private final BankGateway bancos;private final String modo;
    public CelebracionesController(CelebracionesStore s,WorkflowClient c,BankGateway b,@Value("${banking.temporal.mode}")String m){store=s;client=c;bancos=b;modo=m;}
    @GetMapping("/api/celebraciones/saludo") public Map<String,String> saludo(Authentication a){return Map.of("mensaje",store.saludo("bruno".equals(a.getName())?"B001":"A001"));}
    @GetMapping("/api/lab/celebraciones") public Map<String,Object> estado(){var v=new HashMap<>(store.vista());v.put("scheduleExterno","external".equals(modo));return v;}
    private WorkflowOptions opciones(String key){if(key==null||!key.matches("[A-Za-z0-9_-]{1,40}"))throw new ErrorNegocio("VALIDACION","Usa una clave nueva de hasta 40 caracteres.");return WorkflowOptions.newBuilder().setTaskQueue(PortalConfiguration.TASK_QUEUE).setWorkflowId("cumple-"+key).build();}
    public record Noche(String clave,String fecha,int noches,int periodoSegundos){}
    @PostMapping("/api/lab/celebraciones/preview") public Map<String,Object> preview(@RequestBody Noche n){var fecha=LocalDate.parse(n.fecha());var coinciden=new ArrayList<String>();for(String b:List.of("CORDILLERA","PACIFICO"))for(var c:bancos.cuentas(b))if(PoliticaCumple.corresponde(LocalDate.parse(c.nacimiento()),fecha))coinciden.add(c.id());return Map.of("fecha",fecha.toString(),"coincidencias",coinciden);}
    @PostMapping("/api/lab/celebraciones/noche") public Map<String,String> noche(@RequestBody Noche n){String fecha=LocalDate.parse(n.fecha()).toString();var w=client.newWorkflowStub(CumpleWorkflow.class,opciones(n.clave()));WorkflowClient.start(w::ejecutar,fecha);return Map.of("workflowId","cumple-"+n.clave());}
    @PostMapping("/api/lab/celebraciones/ciclo") public Map<String,String> ciclo(@RequestBody Noche n){if(n.noches()<1||n.noches()>365||n.periodoSegundos()<1||n.periodoSegundos()>86400)throw new ErrorNegocio("VALIDACION","Revisa noches y período.");var w=client.newWorkflowStub(CicloCumpleWorkflow.class,opciones(n.clave()));WorkflowClient.start(w::simular,LocalDate.parse(n.fecha()).toString(),n.noches(),n.periodoSegundos(),0);return Map.of("workflowId","cumple-"+n.clave());}
    public record Nacimiento(String fecha){}
    @PostMapping("/api/lab/celebraciones/nacimiento/{banco}/{cuenta}") public Modelos.Cuenta nacimiento(@PathVariable String banco,@PathVariable String cuenta,@RequestBody Nacimiento n){return bancos.nacimiento(banco,cuenta,LocalDate.parse(n.fecha()).toString());}
    @PostMapping("/api/lab/celebraciones/schedule/{accion}") public Map<String,String> agenda(@PathVariable String accion){
        if(!"external".equals(modo))throw new ErrorNegocio("VALIDACION","Schedule requiere Temporal externo; usa Simular noche en modo embebido.");
        var sc=ScheduleClient.newInstance(client.getWorkflowServiceStubs());String id="cumple-diario-"+PortalConfiguration.TASK_QUEUE;
        if("crear".equals(accion)){
            var schedule=Schedule.newBuilder().setAction(ScheduleActionStartWorkflow.newBuilder().setWorkflowType(CumpleWorkflow.class).setArguments("").setOptions(WorkflowOptions.newBuilder().setWorkflowId(id+"-ejecucion").setTaskQueue(PortalConfiguration.TASK_QUEUE).build()).build()).setSpec(AgendaCumple.especificacion()).setPolicy(SchedulePolicy.newBuilder().setOverlap(ScheduleOverlapPolicy.SCHEDULE_OVERLAP_POLICY_SKIP).build()).build();
            try{sc.createSchedule(id,schedule,ScheduleOptions.newBuilder().build());}catch(ScheduleAlreadyRunningException e){return Map.of("scheduleId",id,"estado","YA_EXISTE");}
        }else if("pausar".equals(accion))sc.getHandle(id).pause("Pausa docente");else if("reanudar".equals(accion))sc.getHandle(id).unpause("Reanudación docente");else if("disparar".equals(accion))sc.getHandle(id).trigger();else throw new ErrorNegocio("VALIDACION","Acción de Schedule desconocida.");
        return Map.of("scheduleId",id,"estado",accion);
    }
}
