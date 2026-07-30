package com.sigeo.clase06;

import java.time.LocalDateTime;

// TODO(C06-E06): Crear interface projection para el resumen por responsable
public interface ResumenResponsable {
    String getResponsable();
    Long getCantidadAprobaciones();
    LocalDateTime getUltimaAprobacion();
}
