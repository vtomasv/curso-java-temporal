package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import static org.assertj.core.api.Assertions.*;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
@Tag("lab-e02")
class LabE02Test {
    @Test void repeticionDevuelveReciboOriginal(){var bank=TestStores.bank("PACIFICO");var c=new ComandoBanco("clave","tx","B001",100_000,Direccion.CREDITO);var primero=bank.ejecutar(c);assertThat(bank.ejecutar(c)).isEqualTo(primero);assertThat(bank.cuentas().getFirst().saldo()).isEqualTo(600_000);assertThat(bank.movimientos()).hasSize(1);}
    @Test void mismaClaveDistintoMontoSeRechaza(){var bank=TestStores.bank("PACIFICO");bank.ejecutar(new ComandoBanco("clave","tx","B001",100_000,Direccion.CREDITO));assertThatThrownBy(()->bank.ejecutar(new ComandoBanco("clave","tx","B001",200_000,Direccion.CREDITO))).isInstanceOfSatisfying(ErrorNegocio.class,e->assertThat(e.tipo()).isEqualTo("CLAVE_REUTILIZADA"));assertThat(bank.cuentas().getFirst().saldo()).isEqualTo(600_000);}
    @Test void reciboSobreviveReinicio(@TempDir Path dir){String url="jdbc:h2:file:"+dir.resolve("banco");var bank=TestStores.bankUrl(url,"PACIFICO");var c=new ComandoBanco("clave","tx","B001",100_000,Direccion.CREDITO);var primero=bank.ejecutar(c);var reiniciado=TestStores.bankUrl(url,"PACIFICO");assertThat(reiniciado.ejecutar(c)).isEqualTo(primero);assertThat(reiniciado.movimientos()).hasSize(1);}
    @Test void solicitudesConcurrentesObtienenUnRecibo()throws Exception{var bank=TestStores.bank("PACIFICO");var c=new ComandoBanco("clave","tx","B001",100_000,Direccion.CREDITO);try(var pool=Executors.newFixedThreadPool(4)){List<Future<ReciboBanco>> results=new ArrayList<>();for(int i=0;i<8;i++)results.add(pool.submit(()->bank.ejecutar(c)));ReciboBanco first=results.getFirst().get(10,TimeUnit.SECONDS);for(var r:results)assertThat(r.get(10,TimeUnit.SECONDS)).isEqualTo(first);assertThat(bank.movimientos()).hasSize(1);}}
    @Test void respuestaPerdidaNoDuplicaCredito(){try(var f=new WorkflowFixture()){f.fallos.configurar(new Escenario("RESPUESTA_PERDIDA",1,0));var t=f.crear("e02",100_000);assertThat(f.transferir(t).estado()).isEqualTo("COMPLETADA");assertThat(f.intentos.get(t.id()+":credito").get()).isEqualTo(2);assertThat(f.b.movimientos()).hasSize(1);assertThat(f.saldoB()).isEqualTo(600_000);}}
}
