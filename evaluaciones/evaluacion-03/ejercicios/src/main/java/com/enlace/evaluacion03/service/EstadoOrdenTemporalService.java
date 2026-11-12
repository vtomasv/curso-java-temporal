package com.enlace.evaluacion03.service;

import com.enlace.evaluacion03.domain.EstadoOrden;
import com.enlace.evaluacion03.domain.Orden;
import com.enlace.evaluacion03.repository.OrdenRepository;
import com.enlace.evaluacion03.workflow.EstadoDespacho;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Adaptador invocado por una Activity para reflejar el resultado durable en JPA. */
@Service
public class EstadoOrdenTemporalService {

    private final OrdenRepository ordenes;

    public EstadoOrdenTemporalService(OrdenRepository ordenes) {
        this.ordenes = ordenes;
    }

    @Transactional
    public void registrar(String ordenId, EstadoDespacho estadoDespacho) {
        EstadoOrden estadoOrden = switch (estadoDespacho) {
            case ENTREGADA -> EstadoOrden.ENTREGADA;
            case PENDIENTE_REVISION -> EstadoOrden.PENDIENTE_REVISION;
            case CANCELADA -> EstadoOrden.CANCELADA;
            default -> throw new IllegalArgumentException(
                    "El estado de despacho no es terminal: " + estadoDespacho);
        };

        Orden orden = ordenes.findById(ordenId)
                .orElseThrow(() -> new OrdenNoEncontradaException(ordenId));
        if (orden.getEstado() == estadoOrden) {
            return;
        }
        orden.marcarResultado(estadoOrden);
    }
}
