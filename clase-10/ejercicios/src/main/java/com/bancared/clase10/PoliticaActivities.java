package com.bancared.clase10;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import java.time.Duration;
public final class PoliticaActivities {
    private PoliticaActivities() {}
    public static ActivityOptions operaciones(){
        // TODO(C10-E01): mantener StartToClose=5 s y ScheduleToClose=15 s.
        // Cambiar a 3 intentos, intervalo inicial 200 ms, coeficiente 2 y máximo 1 s.
        // Explicar por qué VALIDACION y RECHAZO_BANCO no deben reintentarse.
        return ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(5)).setScheduleToCloseTimeout(Duration.ofSeconds(15))
            .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(1).setDoNotRetry("VALIDACION","RECHAZO_BANCO","FONDOS_INSUFICIENTES").build()).build();
    }
    public static ActivityOptions persistencia(){return ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(3)).setScheduleToCloseTimeout(Duration.ofSeconds(10)).setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(3).build()).build();}
}
