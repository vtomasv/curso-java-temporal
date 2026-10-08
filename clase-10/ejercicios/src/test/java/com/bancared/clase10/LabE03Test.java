package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.*;
@Tag("lab-e03")
class LabE03Test {
    @Test void reversaRestauraSaldosYConservaOriginal(){try(var f=new WorkflowFixture()){var t=f.crear("e03",100_000);f.transferir(t);assertThat(ReversaWorkflowImpl.disponible()).isTrue();assertThat(f.reversar(t.id()).estado()).isEqualTo("COMPLETADA");assertThat(f.saldoA()).isEqualTo(1_000_000);assertThat(f.saldoB()).isEqualTo(500_000);assertThat(f.portal.obtener(t.id()).estado()).isEqualTo("COMPLETADA");assertThat(f.a.movimientos()).hasSize(2);assertThat(f.b.movimientos()).hasSize(2);}}
    @Test void repetirReversaNoAgregaMovimientos(){try(var f=new WorkflowFixture()){var t=f.crear("repetida",100_000);f.transferir(t);f.reversar(t.id());assertThat(f.reversar(t.id()).estado()).isEqualTo("COMPLETADA");assertThat(f.a.movimientos()).hasSize(2);assertThat(f.b.movimientos()).hasSize(2);}}
    @Test void saldoInsuficienteEnReceptorRechazaReversa(){try(var f=new WorkflowFixture()){var t=f.crear("sin-saldo",100_000);f.transferir(t);f.b.ejecutar(new ComandoBanco("retiro","otra-operacion","B001",550_000,Direccion.DEBITO));assertThat(f.reversar(t.id()).estado()).isEqualTo("RECHAZADA");assertThat(f.saldoA()).isEqualTo(900_000);assertThat(f.saldoB()).isEqualTo(50_000);}}
    @Test void transferenciaNoCompletadaNoEsElegible(){try(var f=new WorkflowFixture()){var t=f.crear("no-elegible",100_000);assertThat(f.reversar(t.id()).estado()).isEqualTo("RECHAZADA");assertThat(f.saldoA()).isEqualTo(1_000_000);}}
}
