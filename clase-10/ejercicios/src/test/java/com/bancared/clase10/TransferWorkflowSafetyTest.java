package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
class TransferWorkflowSafetyTest {
    @Test void transferenciaNormalConservaElTotal(){try(var f=new WorkflowFixture()){assertThat(f.transferir(f.crear("normal",100_000)).estado()).isEqualTo("COMPLETADA");assertThat(f.saldoA()).isEqualTo(900_000);assertThat(f.saldoB()).isEqualTo(600_000);assertThat(f.saldoA()+f.saldoB()).isEqualTo(1_500_000);}}
    @Test void rechazoDefinitivoCompensaElDebito(){try(var f=new WorkflowFixture()){f.fallos.configurar(new Escenario("PERMANENTE",0,0));assertThat(f.transferir(f.crear("rechazo",100_000)).estado()).isEqualTo("COMPENSADA");assertThat(f.saldoA()).isEqualTo(1_000_000);assertThat(f.saldoB()).isEqualTo(500_000);assertThat(f.a.movimientos()).hasSize(2);}}
    @Test void creditoInciertoNoDevuelveDineroACiegas(){try(var f=new WorkflowFixture()){f.fallos.configurar(new Escenario("TRANSITORIO",5,0));f.consultaIncierta=true;assertThat(f.transferir(f.crear("incierta",100_000)).estado()).isEqualTo("PENDIENTE_REVISION");assertThat(f.saldoA()).isEqualTo(900_000);assertThat(f.a.movimientos()).hasSize(1);}}
    @Test void fondosInsuficientesNoAcreditanDestino(){try(var f=new WorkflowFixture()){assertThat(f.transferir(f.crear("sin-fondos",1_100_000)).estado()).isEqualTo("RECHAZADA");assertThat(f.saldoA()).isEqualTo(1_000_000);assertThat(f.saldoB()).isEqualTo(500_000);}}
    @Test void solicitudRepetidaTieneMismoId(){var store=TestStores.portal();var s=new SolicitudTransferencia("misma-clave","CORDILLERA","A001","PACIFICO","B001",100_000,"Prueba");assertThat(store.crear(s).id()).isEqualTo(store.crear(s).id());assertThat(store.todas()).hasSize(1);assertThatThrownBy(()->store.crear(new SolicitudTransferencia("misma-clave","CORDILLERA","A001","PACIFICO","B001",200_000,"Prueba"))).isInstanceOf(ErrorNegocio.class);}
}
