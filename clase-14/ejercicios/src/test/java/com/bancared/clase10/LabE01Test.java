package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import static org.assertj.core.api.Assertions.*;
import java.time.Duration;
import org.junit.jupiter.api.*;
@Tag("base-c10-e01")
class LabE01Test {
    @Test void politicaAcotada(){var p=PoliticaActivities.operaciones();assertThat(p.getStartToCloseTimeout()).isEqualTo(Duration.ofSeconds(5));assertThat(p.getScheduleToCloseTimeout()).isEqualTo(Duration.ofSeconds(15));assertThat(p.getRetryOptions().getMaximumAttempts()).isEqualTo(3);assertThat(p.getRetryOptions().getInitialInterval()).isEqualTo(Duration.ofMillis(200));assertThat(p.getRetryOptions().getBackoffCoefficient()).isEqualTo(2);assertThat(p.getRetryOptions().getMaximumInterval()).isEqualTo(Duration.ofSeconds(1));}
    @Test void dos503YElTercerIntentoCompleta(){try(var f=new WorkflowFixture()){f.fallos.configurar(new Escenario("TRANSITORIO",2,0));var t=f.crear("e01",100_000);assertThat(f.transferir(t).estado()).isEqualTo("COMPLETADA");assertThat(f.intentos.get(t.id()+":credito").get()).isEqualTo(3);assertThat(f.saldoA()).isEqualTo(900_000);assertThat(f.saldoB()).isEqualTo(600_000);}}
    @Test void rechazoPermanenteNoSeReintenta(){try(var f=new WorkflowFixture()){f.fallos.configurar(new Escenario("PERMANENTE",0,0));var t=f.crear("permanente",100_000);f.transferir(t);assertThat(f.intentos.get(t.id()+":credito").get()).isEqualTo(1);assertThat(f.saldoA()).isEqualTo(1_000_000);}}
}
