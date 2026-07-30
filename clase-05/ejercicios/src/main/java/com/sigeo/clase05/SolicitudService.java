package com.sigeo.clase05;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudService {

    // TODO(C05-E02): Inyectar SolicitudRepository por constructor
    private final SolicitudRepository repository;

    public SolicitudService(SolicitudRepository repository) {
        this.repository = repository;
    }
    
    public Solicitud crearSolicitud(String titulo, String descripcion, String prioridad) {
        // TODO(C05-E02): Crear una nueva solicitud con estado "CREADA" y guardarla en el repositorio
        throw new UnsupportedOperationException("TODO C05-E02");
    }

    public Solicitud obtenerPorId(Long id) {
        // TODO(C05-E04): Buscar por ID, lanzar SolicitudNotFoundException si no existe
        throw new UnsupportedOperationException("TODO C05-E04");
    }

    public List<Solicitud> buscarSolicitudes(String estado, String prioridad) {
        // TODO(C05-E04): Usar el repositorio para buscar por estado y prioridad
        throw new UnsupportedOperationException("TODO C05-E04");
    }
}
