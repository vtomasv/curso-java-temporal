package com.enlace.evaluacion03.service;

import com.enlace.evaluacion03.domain.Orden;
import com.enlace.evaluacion03.repository.FirmaOrdenRepository;
import com.enlace.evaluacion03.repository.OrdenRepository;
import com.enlace.evaluacion03.web.CrearOrdenRequest;
import com.enlace.evaluacion03.web.OrdenDetalle;
import com.enlace.evaluacion03.web.OrdenResumen;
import com.enlace.evaluacion03.workflow.DespachoInput;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrdenService {

    private final OrdenRepository ordenes;
    private final FirmaOrdenRepository firmas;
    private final PoliticaAcceso acceso;
    private final PoliticaFirmas politicaFirmas;

    public OrdenService(OrdenRepository ordenes, FirmaOrdenRepository firmas,
                        PoliticaAcceso acceso, PoliticaFirmas politicaFirmas) {
        this.ordenes = ordenes;
        this.firmas = firmas;
        this.acceso = acceso;
        this.politicaFirmas = politicaFirmas;
    }

    @Transactional
    public OrdenResumen crear(CrearOrdenRequest request, String creador) {
        return ordenes.findByClaveIdempotencia(request.claveIdempotencia())
                .map(this::resumen)
                .orElseGet(() -> resumen(ordenes.save(new Orden(
                        request.id(), request.asunto(), request.contenido(), request.tipo(),
                        request.confidencialidad(), request.destinatarios(), creador,
                        request.claveIdempotencia()
                ))));
    }

    @Transactional(readOnly = true)
    public List<OrdenResumen> listar() {
        return ordenes.findAllByOrderByCreadaEnDesc().stream().map(this::resumen).toList();
    }

    @Transactional(readOnly = true)
    public OrdenResumen resumen(String id) {
        return resumen(buscar(id));
    }

    @Transactional(readOnly = true)
    public OrdenResumen resumenAutorizado(String id, Authentication authentication) {
        Orden orden = buscar(id);
        if (!acceso.puedeLeer(authentication, orden)) {
            throw new AccesoDenegadoException("No tiene permiso para leer la orden " + id);
        }
        return resumen(orden);
    }

    @Transactional(readOnly = true)
    public OrdenDetalle detalle(String id, Authentication authentication) {
        Orden orden = buscar(id);
        if (!acceso.puedeLeer(authentication, orden)) {
            throw new AccesoDenegadoException("No tiene permiso para leer la orden " + id);
        }
        return new OrdenDetalle(
                resumen(orden), orden.getContenido(), orden.getCreador(),
                firmas.findByOrdenIdOrderByFirmadaEnAsc(id)
        );
    }

    /** Construye el input mínimo; nunca incorpora el contenido confidencial. */
    @Transactional(readOnly = true)
    public DespachoInput prepararWorkflow(String id) {
        Orden orden = buscar(id);
        return new DespachoInput(
                orden.getId(),
                orden.getDestinatarios().stream().map(Enum::name).toList()
        );
    }

    private Orden buscar(String id) {
        return ordenes.findById(id).orElseThrow(() -> new OrdenNoEncontradaException(id));
    }

    private OrdenResumen resumen(Orden orden) {
        long cantidadFirmas = firmas.countByOrdenId(orden.getId());
        return new OrdenResumen(
                orden.getId(), orden.getAsunto(), orden.getTipo(), orden.getConfidencialidad(),
                orden.getDestinatarios(), orden.getEstado(), cantidadFirmas,
                politicaFirmas.firmasRequeridas(orden)
        );
    }
}
