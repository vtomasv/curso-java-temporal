package com.sigeo.clase04;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

class ProcesadorParaleloTest {

    @Test
    void debeProcesarYSumarCorrectamente() throws InterruptedException, ExecutionException {
        ProcesadorParalelo procesador = new ProcesadorParalelo();
        List<Solicitud> solicitudes = new ArrayList<>();
        
        for (int i = 0; i < 100; i++) {
            solicitudes.add(new Solicitud("REQ-" + i, "Desc", "NUEVO", 1, 2));
        }
        
        long inicio = System.currentTimeMillis();
        int totalHoras = procesador.procesarYSumarHoras(solicitudes);
        long fin = System.currentTimeMillis();
        
        System.out.println("Total horas: " + totalHoras);
        assertThat(totalHoras).isEqualTo(200);
        
        // Si fuera secuencial tomaría ~5000ms. Con virtual threads debería ser mucho menos.
        System.out.println("Tiempo de procesamiento: " + (fin - inicio) + " ms");
        assertThat(fin - inicio).isLessThan(2000);
    }
}
