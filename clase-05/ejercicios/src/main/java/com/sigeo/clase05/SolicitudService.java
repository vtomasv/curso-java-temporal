package com.sigeo.clase05;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudService {

    private final SolicitudRepository repository;

    public SolicitudService(SolicitudRepository repository) {
        this.repository = repository;
    }
    
    public Solicitud crearSolicitud(String titulo, String descripcion, String prioridad) {
        Solicitud solicitud = new Solicitud(null, titulo, descripcion, "CREADA", prioridad);
        return repository.save(solicitud);
    }

    public Solicitud obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new SolicitudNotFoundException(id));
    }

    public List<Solicitud> buscarSolicitudes(String estado, String prioridad) {
        return this.repository.findByEstadoAndPrioridad(estado, prioridad);
    }

    public List<Solicitud> listarTodas() {
        return repository.findAll();
    }
}
