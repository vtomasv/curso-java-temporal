package com.sigeo.clase02;

import java.time.LocalDateTime;

public enum Prioridad {
    // TODO(C02-E03): Definir valores BAJA, MEDIA, ALTA, CRITICA
    // Cada valor debe tener horas de atención (ej: 72, 48, 24, 4)
    // y un factor de escalamiento (ej: 1.0, 1.5, 2.0, 3.0)
    
    BAJA, MEDIA, ALTA, CRITICA; // Reemplazar con constructores
    
    // TODO(C02-E03): Implementar método deadlineFrom(LocalDateTime inicio)
    // que sume las horas de atención a la fecha de inicio
    public LocalDateTime deadlineFrom(LocalDateTime inicio) {
        throw new UnsupportedOperationException("TODO C02-E03");
    }
    
    // TODO(C02-E03): Implementar getters para horasAtencion y factorEscalamiento
    public int getHorasAtencion() {
        throw new UnsupportedOperationException("TODO C02-E03");
    }
    
    public double getFactorEscalamiento() {
        throw new UnsupportedOperationException("TODO C02-E03");
    }
}
