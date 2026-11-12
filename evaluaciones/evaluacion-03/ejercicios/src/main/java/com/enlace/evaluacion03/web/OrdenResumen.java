package com.enlace.evaluacion03.web;

import com.enlace.evaluacion03.domain.Confidencialidad;
import com.enlace.evaluacion03.domain.EstadoOrden;
import com.enlace.evaluacion03.domain.TipoOrden;
import com.enlace.evaluacion03.domain.UnidadDestino;

import java.util.Set;

public record OrdenResumen(
        String id,
        String asunto,
        TipoOrden tipo,
        Confidencialidad confidencialidad,
        Set<UnidadDestino> destinatarios,
        EstadoOrden estado,
        long firmas,
        long firmasRequeridas
) {
}
