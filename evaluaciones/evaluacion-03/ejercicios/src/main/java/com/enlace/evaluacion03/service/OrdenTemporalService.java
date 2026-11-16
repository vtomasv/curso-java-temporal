package com.enlace.evaluacion03.service;

import com.enlace.evaluacion03.domain.EstadoOrden;
import com.enlace.evaluacion03.domain.FirmaOrden;
import com.enlace.evaluacion03.domain.Orden;
import com.enlace.evaluacion03.repository.FirmaOrdenRepository;
import com.enlace.evaluacion03.repository.OrdenRepository;
import com.enlace.evaluacion03.workflow.EstadoDespacho;
import com.enlace.evaluacion03.workflow.FirmaProgreso;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * Puerto transaccional usado exclusivamente por Activities de Temporal. La
 * interfaz web no persiste firmas ni adelanta estados de despacho directamente.
 */
@Service
public class OrdenTemporalService {

    private final OrdenRepository ordenes;
    private final FirmaOrdenRepository firmas;
    private final PoliticaFirmas politicaFirmas;

    public OrdenTemporalService(OrdenRepository ordenes, FirmaOrdenRepository firmas,
                                PoliticaFirmas politicaFirmas) {
        this.ordenes = ordenes;
        this.firmas = firmas;
        this.politicaFirmas = politicaFirmas;
    }

    @Transactional(readOnly = true)
    public FirmaProgreso consultarFirmas(String ordenId) {
        Orden orden = buscar(ordenId);
        return progreso(orden, firmas.findByOrdenIdOrderByFirmadaEnAsc(ordenId));
    }

    /**
     * Registra una firma con semántica idempotente para tolerar reintentos de la
     * Activity. Un segundo Update del usuario se rechaza en el Workflow antes de
     * invocar esta operación; un retry de la misma Activity devuelve el estado ya
     * persistido.
     */
    @Transactional
    public FirmaProgreso registrarFirma(String ordenId, String firmante, String rol) {
        Orden orden = buscar(ordenId);
        var existente = firmas.findByOrdenIdAndFirmante(ordenId, firmante);
        if (existente.isPresent()) {
            if (!existente.orElseThrow().getRol().equals(rol)) {
                throw new IllegalArgumentException(
                        "La firma existente de " + firmante + " pertenece a otro rol");
            }
            return progreso(orden, firmas.findByOrdenIdOrderByFirmadaEnAsc(ordenId));
        }
        if (orden.getEstado() != EstadoOrden.PENDIENTE_FIRMAS) {
            throw new IllegalStateException("La orden " + ordenId + " no admite más firmas");
        }
        if (!politicaFirmas.rolPuedeFirmar(orden, rol)) {
            throw new IllegalArgumentException("El rol " + rol + " no puede firmar esta orden");
        }

        firmas.saveAndFlush(new FirmaOrden(ordenId, firmante, rol));
        List<FirmaOrden> actuales = firmas.findByOrdenIdOrderByFirmadaEnAsc(ordenId);
        if (politicaFirmas.requisitoCumplido(orden, actuales)) {
            orden.marcarListaParaEnvio();
        }
        return progreso(orden, actuales);
    }

    @Transactional
    public void iniciarDespacho(String ordenId) {
        Orden orden = buscar(ordenId);
        if (orden.getEstado() == EstadoOrden.LISTA_PARA_ENVIO) {
            orden.marcarEnDespacho();
            return;
        }
        if (orden.getEstado() != EstadoOrden.EN_DESPACHO) {
            throw new IllegalStateException(
                    "La orden " + ordenId + " no completó su política de firmas");
        }
    }

    @Transactional
    public void registrarEstado(String ordenId, EstadoDespacho estadoDespacho) {
        EstadoOrden estadoOrden = switch (estadoDespacho) {
            case ENTREGADA -> EstadoOrden.ENTREGADA;
            case PENDIENTE_REVISION -> EstadoOrden.PENDIENTE_REVISION;
            case CANCELADA -> EstadoOrden.CANCELADA;
            default -> throw new IllegalArgumentException(
                    "El estado de despacho no es terminal: " + estadoDespacho);
        };

        Orden orden = buscar(ordenId);
        if (orden.getEstado() == estadoOrden) return;
        orden.marcarResultado(estadoOrden);
    }

    private FirmaProgreso progreso(Orden orden, List<FirmaOrden> actuales) {
        int requeridas = politicaFirmas.firmasRequeridas(orden);
        boolean completa = politicaFirmas.requisitoCumplido(orden, actuales);
        LinkedHashSet<String> firmantes = actuales.stream()
                .map(FirmaOrden::getFirmante)
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
        return new FirmaProgreso(firmantes.size(), requeridas, completa, firmantes);
    }

    private Orden buscar(String ordenId) {
        return ordenes.findById(ordenId)
                .orElseThrow(() -> new OrdenNoEncontradaException(ordenId));
    }
}
