package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/** HTTP con timeout propio, además del límite de la Activity. */
public class BankGateway {
    private final Map<String,RestClient> clientes;
    public BankGateway(String cordillera,String pacifico,String password){clientes=Map.of("CORDILLERA",cliente(cordillera,password),"PACIFICO",cliente(pacifico,password));}
    private RestClient cliente(String url,String password){
        var factory=new JdkClientHttpRequestFactory(HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build());
        factory.setReadTimeout(Duration.ofSeconds(3));
        return RestClient.builder().baseUrl(url).requestFactory(factory).defaultHeaders(h->h.setBasicAuth("intermediario",password)).build();
    }
    private RestClient banco(String codigo){RestClient c=clientes.get(codigo);if(c==null)throw new ErrorNegocio("VALIDACION","Banco desconocido.");return c;}
    public ReciboBanco ejecutar(String banco,ComandoBanco c){return banco(banco).post().uri("/bank/commands").body(c).retrieve().body(ReciboBanco.class);}
    public ConsultaComando consultar(String banco,String id){return banco(banco).get().uri("/bank/commands/{id}",id).retrieve().body(ConsultaComando.class);}
    public List<Cuenta> cuentas(String banco){return banco(banco).get().uri("/bank/accounts").retrieve().body(new ParameterizedTypeReference<List<Cuenta>>(){});}
    public List<ReciboBanco> movimientos(String banco){return banco(banco).get().uri("/bank/movements").retrieve().body(new ParameterizedTypeReference<List<ReciboBanco>>(){});}
    public Escenario configurar(String banco,Escenario e){return banco(banco).post().uri("/bank/faults").body(e).retrieve().body(Escenario.class);}
    public Cuenta nacimiento(String banco,String cuenta,String fecha){return banco(banco).post().uri("/bank/accounts/{id}/birthdate",cuenta).body(java.util.Map.of("fecha",fecha)).retrieve().body(Cuenta.class);}
    public LiquidacionModelos.Liquidez liquidez(String banco){return banco(banco).get().uri("/bank/liquidity").retrieve().body(LiquidacionModelos.Liquidez.class);}
    public LiquidacionModelos.ReciboLiquidez moverLiquidez(String banco,LiquidacionModelos.OrdenLiquidez c){return banco(banco).post().uri("/bank/liquidity/commands").body(c).retrieve().body(LiquidacionModelos.ReciboLiquidez.class);}
    public boolean confirmarLiquidez(String banco,String clave){var r=banco(banco).get().uri("/bank/liquidity/commands/{id}",clave).retrieve().body(java.util.Map.class);return Boolean.TRUE.equals(r.get("aplicado"));}
}