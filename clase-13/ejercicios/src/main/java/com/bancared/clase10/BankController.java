package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;
@RestController @ConditionalOnProperty(name="banking.role",havingValue="bank") @RequestMapping("/bank")
public class BankController {
    private final BankStore store; private final FallosBanco fallos;
    public BankController(BankStore store,FallosBanco fallos){this.store=store;this.fallos=fallos;}
    @GetMapping("/accounts") public List<Cuenta> cuentas(){return store.cuentas();}
    @GetMapping("/movements") public List<ReciboBanco> movimientos(){return store.movimientos();}
    @GetMapping("/commands/{id}") public ConsultaComando consultar(@PathVariable String id){return store.consultar(id);}
    @PostMapping("/commands") public ReciboBanco ejecutar(@RequestBody ComandoBanco c){fallos.antes(c);ReciboBanco r=store.ejecutar(c);fallos.despues(c);return r;}
    @PostMapping("/faults") public Escenario configurar(@RequestBody Escenario e){return fallos.configurar(e);}
    @GetMapping("/faults") public Escenario escenario(){return fallos.actual();}
    @PostMapping("/accounts/{id}/birthdate") public Cuenta nacimiento(@PathVariable String id,@RequestBody java.util.Map<String,String> fecha){return store.nacimiento(id,fecha.get("fecha"));}
}