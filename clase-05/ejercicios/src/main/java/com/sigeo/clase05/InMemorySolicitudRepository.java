package com.sigeo.clase05;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemorySolicitudRepository implements SolicitudRepository {

    private final List<Solicitud> solicitudes = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Solicitud save(Solicitud solicitud) {
        // TODO(C05-E02): Implementar guardado en memoria. Si el id es null, asignar uno nuevo usando idGenerator.
        throw new UnsupportedOperationException("TODO C05-E02");
    }

    @Override
    public Optional<Solicitud> findById(Long id) {
        // TODO(C05-E02): Implementar búsqueda por ID
        throw new UnsupportedOperationException("TODO C05-E02");
    }

    @Override
    public List<Solicitud> findAll() {
        // TODO(C05-E02): Retornar todas las solicitudes
        throw new UnsupportedOperationException("TODO C05-E02");
    }

    @Override
    public List<Solicitud> findByEstadoAndPrioridad(String estado, String prioridad) {
        // TODO(C05-E04): Implementar filtrado por estado y/o prioridad (si son null, no filtrar por ese campo)
        throw new UnsupportedOperationException("TODO C05-E04");
    }
}
