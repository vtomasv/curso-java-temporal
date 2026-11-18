package com.enlace.evaluacion03.service;

import com.enlace.evaluacion03.domain.EstadoOrden;
import com.enlace.evaluacion03.domain.FirmaOrden;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        return bandeja(null, null).ordenes();
    }

    public record Bandeja(List<OrdenResumen> ordenes, Set<String> firmables) {}

    @Transactional(readOnly = true)
    public Bandeja bandeja(String usuario, String rol) {
        var porOrden = firmas.findAll().stream().collect(Collectors.groupingBy(FirmaOrden::getOrdenId));
        var resumenes = new ArrayList<OrdenResumen>();
        var firmables = new HashSet<String>();
        for (Orden orden : ordenes.findAllByOrderByCreadaEnDesc()) {
            var actuales = porOrden.getOrDefault(orden.getId(), List.of());
            resumenes.add(resumen(orden, actuales.size()));
            if (puedeFirmar(orden, usuario, rol, actuales)) firmables.add(orden.getId());
        }
        return new Bandeja(List.copyOf(resumenes), Set.copyOf(firmables));
    }

    @Transactional(readOnly = true)
    public boolean puedeLeer(String id, Authentication authentication) {
        return acceso.puedeLeer(authentication, buscar(id));
    }

    @Transactional(readOnly = true)
    public boolean puedeFirmar(String id, String usuario, String rol) {
        return puedeFirmar(buscar(id), usuario, rol, firmas.findByOrdenIdOrderByFirmadaEnAsc(id));
    }

    private boolean puedeFirmar(Orden orden, String usuario, String rol, List<FirmaOrden> actuales) {
        return orden.getEstado() == EstadoOrden.PENDIENTE_FIRMAS
                && politicaFirmas.faltaFirmaDelRol(orden, rol, actuales)
                && actuales.stream().noneMatch(firma -> firma.getFirmante().equals(usuario));
    }

    @Transactional(readOnly = true)
    public DespachoInput prepararFirma(String id, String rol) {
        Orden orden = buscar(id);
        if (!politicaFirmas.rolPuedeFirmar(orden, rol)) {
            throw new AccesoDenegadoException("Su rol no puede firmar el despacho de " + id);
        }
        return inputDespacho(orden);
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
        return inputDespacho(buscar(id));
    }

    private DespachoInput inputDespacho(Orden orden) {
        return new DespachoInput(
                orden.getId(),
                orden.getDestinatarios().stream().map(Enum::name).toList()
        );
    }

    private Orden buscar(String id) {
        return ordenes.findById(id).orElseThrow(() -> new OrdenNoEncontradaException(id));
    }

    private OrdenResumen resumen(Orden orden) {
        return resumen(orden, firmas.countByOrdenId(orden.getId()));
    }

    private OrdenResumen resumen(Orden orden, long cantidadFirmas) {
        return new OrdenResumen(
                orden.getId(), orden.getAsunto(), orden.getTipo(), orden.getConfidencialidad(),
                orden.getDestinatarios(), orden.getEstado(), cantidadFirmas,
                politicaFirmas.firmasRequeridas(orden)
        );
    }
}
