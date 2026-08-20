package com.sigeo.clase04;

import java.util.List;

public class Deduplicador {

    /**
     * Elimina duplicados de una lista de solicitudes conservando el orden de llegada.
     * Dos solicitudes se consideran iguales si tienen el mismo ID.
     * 
     * @param solicitudes Lista original con posibles duplicados
     * @return Lista sin duplicados, manteniendo el orden original
     */
    public List<Solicitud> deduplicar(List<Solicitud> solicitudes) {
        java.util.Set<Solicitud> uniqueSolicitudes = new java.util.LinkedHashSet<>(solicitudes);
        return new java.util.ArrayList<>(uniqueSolicitudes);

    }
}
