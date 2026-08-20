package com.sigeo.clase04;

import java.util.List;
import java.util.Map;

public class IndiceResponsables {

    private final Map<Responsable, List<Solicitud>> indice;

    public IndiceResponsables(Map<Responsable, List<Solicitud>> asignaciones) {
        this.indice = Map.copyOf(asignaciones);
    }

    /**
     * Obtiene las solicitudes asignadas a un responsable.
     * Si el responsable no existe en el índice, debe retornar una lista vacía, nunca null.
     * 
     * @param responsable El responsable a consultar
     * @return Lista inmodificable de solicitudes
     */
    public List<Solicitud> obtenerSolicitudes(Responsable responsable) {
        return indice.getOrDefault(responsable, List.of());
    }
}
