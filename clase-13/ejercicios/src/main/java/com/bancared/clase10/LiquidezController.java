package com.bancared.clase10;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import static com.bancared.clase10.LiquidacionModelos.*;
@RestController @ConditionalOnProperty(name="banking.role",havingValue="bank") @RequestMapping("/bank/liquidity")
public class LiquidezController {
    private final LiquidezStore store;private final FallosBanco fallos;
    public LiquidezController(JdbcTemplate j,FallosBanco f){store=new LiquidezStore(j);fallos=f;}
    @GetMapping public Liquidez vista(){return store.vista();}
    @GetMapping("/commands/{id}") public java.util.Map<String,Object> consulta(@PathVariable String id){var r=store.consultar(id);return r==null?java.util.Map.of("aplicado",false):java.util.Map.of("aplicado",true,"recibo",r);}
    @PostMapping("/commands") public ReciboLiquidez ejecutar(@RequestBody OrdenLiquidez c){var simulado=new Modelos.ComandoBanco(c.clave(),c.lote(),"LIQUIDEZ",Math.abs(c.delta()),c.delta()>0?Modelos.Direccion.CREDITO:Modelos.Direccion.DEBITO);fallos.antes(simulado);var r=store.mover(c);fallos.despues(simulado);return r;}
}
