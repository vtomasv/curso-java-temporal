package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import static org.assertj.core.api.Assertions.*;
import java.util.concurrent.*;
import java.util.*;
import org.junit.jupiter.api.Test;
class BankStoreSafetyTest {
    @Test void montoNegativoNoMueveDinero(){var bank=TestStores.bank("CORDILLERA");assertThatThrownBy(()->bank.ejecutar(new ComandoBanco("k","t","A001",-1,Direccion.DEBITO))).isInstanceOf(ErrorNegocio.class);assertThat(bank.cuentas().getFirst().saldo()).isEqualTo(1_000_000);assertThat(bank.movimientos()).isEmpty();}
    @Test void fondosInsuficientesConservanSaldo(){var bank=TestStores.bank("CORDILLERA");assertThatThrownBy(()->bank.ejecutar(new ComandoBanco("k","t","A001",1_100_000,Direccion.DEBITO))).isInstanceOf(ErrorNegocio.class);assertThat(bank.cuentas().getFirst().saldo()).isEqualTo(1_000_000);}
    @Test void duplicadoNuncaProduceSegundoDebito(){var bank=TestStores.bank("CORDILLERA");var c=new ComandoBanco("k","t","A001",100_000,Direccion.DEBITO);bank.ejecutar(c);try{bank.ejecutar(c);}catch(ErrorNegocio e){assertThat(e.tipo()).isEqualTo("COMANDO_DUPLICADO");}assertThat(bank.cuentas().getFirst().saldo()).isEqualTo(900_000);assertThat(bank.movimientos()).hasSize(1);}
    @Test void cambiarClaveNoDuplicaEfectoEconomico(){var bank=TestStores.bank("CORDILLERA");bank.ejecutar(new ComandoBanco("k1","t","A001",100_000,Direccion.DEBITO));assertThatThrownBy(()->bank.ejecutar(new ComandoBanco("k2","t","A001",100_000,Direccion.DEBITO))).isInstanceOf(ErrorNegocio.class);assertThat(bank.movimientos()).hasSize(1);}
    @Test void dosDebitosConcurrentesNoSobregiranCuenta()throws Exception{
        var bank=TestStores.bank("CORDILLERA");var gate=new CountDownLatch(1);
        try(var pool=Executors.newFixedThreadPool(2)){
            List<Future<Boolean>> results=new ArrayList<>();
            for(int i=0;i<2;i++){int id=i;results.add(pool.submit(()->{gate.await();try{bank.ejecutar(new ComandoBanco("k"+id,"t"+id,"A001",800_000,Direccion.DEBITO));return true;}catch(ErrorNegocio e){return false;}}));}
            gate.countDown();int applied=0;for(var r:results)if(r.get(10,TimeUnit.SECONDS))applied++;
            assertThat(applied).isEqualTo(1);assertThat(bank.cuentas().getFirst().saldo()).isEqualTo(200_000);assertThat(bank.movimientos()).hasSize(1);
        }
    }
    @Test void bancoDestinoNoAccedeALaCuentaDelOrigen(){var bank=TestStores.bank("PACIFICO");assertThatThrownBy(()->bank.ejecutar(new ComandoBanco("k","t","A001",100_000,Direccion.DEBITO))).isInstanceOf(ErrorNegocio.class);assertThat(bank.cuentas().getFirst().saldo()).isEqualTo(500_000);}
}
