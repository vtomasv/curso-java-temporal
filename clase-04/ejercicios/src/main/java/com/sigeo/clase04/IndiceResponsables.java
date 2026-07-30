package com.sigeo.clase04;

import java.util.List;
import java.util.Map;

public class IndiceResponsables {

    private final Map<Responsable, List<Solicitud>> indice;

    public IndiceResponsables(Map<Responsable, List<Solicitud>> asignaciones) {
        // TODO(C04-E02): Inicializar el índice asegurando que no se pueda modificar externamente
        // Pista: Map.copyOf o Collections.unmodifiableMap
        throw new UnsupportedOperationException("TODO C04-E02");
    }

    /**
     * Obtiene las solicitudes asignadas a un responsable.
     * Si el responsable no existe en el índice, debe retornar una lista vacía, nunca null.
     * 
     * @param responsable El responsable a consultar
     * @return Lista inmodificable de solicitudes
     */
    public List<Solicitud> obtenerSolicitudes(Responsable responsable) {
        // TODO(C04-E02): Implementar la consulta segura
        // Pista: getOrDefault
        throw new UnsupportedOperationException("TODO C04-E02");
    }
}
