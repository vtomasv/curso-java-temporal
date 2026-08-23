package com.sigeo.evaluacion01;

/** Prioridad y plazo referencial de atención de una solicitud. */
public enum Prioridad {
    BAJA(72),
    MEDIA(48),
    ALTA(24),
    CRITICA(4);

    private final int horasAtencion;

    Prioridad(int horasAtencion) {
        this.horasAtencion = horasAtencion;
    }

    public int horasAtencion() {
        // TODO(EV01-E01): Retornar las horas asociadas al valor del enum.
        return 0;
    }
}

