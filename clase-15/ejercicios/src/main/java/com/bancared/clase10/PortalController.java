package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller @ConditionalOnProperty(name="banking.role",havingValue="portal")
public class PortalController {
    private final TransferStore store;private final BankGateway gateway;private final PortalService service;private final String mode;private final String ui;
    public PortalController(TransferStore store,BankGateway gateway,PortalService service,@Value("${banking.temporal.mode}")String mode,@Value("${banking.temporal.ui}")String ui){this.store=store;this.gateway=gateway;this.service=service;this.mode=mode;this.ui=ui;}
    private boolean docente(Authentication a){return a.getAuthorities().stream().anyMatch(r->r.getAuthority().equals("ROLE_DOCENTE"));}
    private String cuenta(Authentication a){return "ana".equals(a.getName())?"A001":"B001";}
    private void autorizarOrigen(Authentication a,String cuenta){if(!docente(a) && !cuenta(a).equals(cuenta))throw new ErrorNegocio("NO_AUTORIZADO","Solo puedes operar tu cuenta.");}
    @GetMapping("/") public String inicio(Authentication a,Model m){m.addAttribute("usuario",a.getName());m.addAttribute("docente",docente(a));return "index";}
    @GetMapping("/api/state") @ResponseBody public Map<String,Object> estado(Authentication a){
        List<VistaBanco> bancos=new ArrayList<>();
        for(String codigo:List.of("CORDILLERA","PACIFICO")){
            try{
                var cuentas=gateway.cuentas(codigo).stream().filter(c->docente(a)||c.id().equals(cuenta(a))).toList();
                var movimientos=gateway.movimientos(codigo).stream().filter(r->docente(a)||r.cuentaId().equals(cuenta(a))).toList();
                bancos.add(new VistaBanco(codigo,true,cuentas,movimientos));
            }catch(org.springframework.web.client.RestClientException e){bancos.add(new VistaBanco(codigo,false,List.of(),List.of()));}
        }
        var transferencias=store.todas().stream().filter(t->docente(a)||t.cuentaOrigen().equals(cuenta(a))||t.cuentaDestino().equals(cuenta(a))).toList();
        Set<String> visibles=new HashSet<>(transferencias.stream().map(Transferencia::id).toList());
        return Map.of("bancos",bancos,"transferencias",transferencias,"intentos",store.intentos().stream().filter(i->docente(a)||visibles.contains(i.transferId())||visibles.contains(i.transferId().replace("-reversa",""))).toList(),"reversaDisponible",ReversaWorkflowImpl.disponible(),"temporalMode",mode,"temporalUi",ui,"usuario",a.getName(),"docente",docente(a));
    }
    @PostMapping("/api/transfers") @ResponseBody public Transferencia crear(@RequestBody SolicitudTransferencia s,Authentication a){autorizarOrigen(a,s.cuentaOrigen());return service.iniciar(s);}
    @PostMapping("/api/transfers/{id}/reverse") @ResponseBody public Transferencia reversar(@PathVariable String id,Authentication a){autorizarOrigen(a,store.obtener(id).cuentaOrigen());return service.reversar(id);}
    @PostMapping("/api/lab/faults/{banco}") @ResponseBody public Escenario fallos(@PathVariable String banco,@RequestBody Escenario e){return gateway.configurar(banco,e);}
}
