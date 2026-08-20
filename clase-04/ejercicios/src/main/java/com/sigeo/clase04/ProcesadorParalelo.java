package com.sigeo.clase04;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class ProcesadorParalelo {

    private AtomicInteger contadorSeguro = new AtomicInteger(0);

    /**
     * Procesa una lista de solicitudes en paralelo usando Virtual Threads.
     * Simula un procesamiento que toma 50ms por solicitud.
     * Debe retornar la suma total de las horas estimadas de todas las solicitudes procesadas.
     * 
     * @param solicitudes Lista de solicitudes a procesar
     * @return Suma total de horas estimadas
     * @throws ExecutionException 
     * @throws InterruptedException 
     */
    public int procesarYSumarHoras(List<Solicitud> solicitudes) throws InterruptedException, ExecutionException {
        // TODO(C04-E06): Implementar procesamiento paralelo con Virtual Threads
        // Pista: Executors.newVirtualThreadPerTaskExecutor(), Future, o StructuredTaskScope (si se usa preview)
        // Asegurar que no haya race conditions al sumar.
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        contadorSeguro.set(0);
        try {
            for (Solicitud solicitud : solicitudes) {
                executor.submit(() -> {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } // Simula procesamiento
                    contadorSeguro.addAndGet(solicitud.horasEstimadas());
                }); // Espera a que termine la tarea antes de continuar   
            }
            executor.shutdown();
            while (!executor.isTerminated()) {
                Thread.sleep(10);
            }
            return contadorSeguro.get();  
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar solicitudes", e);
        } finally {
            executor.shutdown();
        }
         
    }
}
