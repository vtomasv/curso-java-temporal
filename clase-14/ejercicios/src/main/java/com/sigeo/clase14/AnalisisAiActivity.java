package com.sigeo.clase14;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface AnalisisAiActivity {

    record AnalisisRequest(String texto, String promptVersion) {}
    record AnalisisResponse(String resultado, String modeloUsado) {}

    /**
     * E06: Análisis durable (Activity)
     * Llamar al modelo desde Activity.
     */
    @ActivityMethod
    AnalisisResponse analizarTexto(AnalisisRequest request);
}
