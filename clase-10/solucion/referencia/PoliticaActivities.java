package com.bancared.clase10;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import java.time.Duration;
public final class PoliticaActivities {
    private PoliticaActivities() {}
    public static ActivityOptions operaciones(){
        return ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(5)).setScheduleToCloseTimeout(Duration.ofSeconds(15))
            .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(3).setInitialInterval(Duration.ofMillis(200)).setBackoffCoefficient(2).setMaximumInterval(Duration.ofSeconds(1)).setDoNotRetry("VALIDACION","RECHAZO_BANCO","FONDOS_INSUFICIENTES").build()).build();
    }
    public static ActivityOptions persistencia(){return ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(3)).setScheduleToCloseTimeout(Duration.ofSeconds(10)).setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(3).build()).build();}
}
