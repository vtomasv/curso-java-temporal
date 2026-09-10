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

        if (solicitud.getId() == null) {
            solicitud.setId(idGenerator.getAndIncrement());
        }
        solicitudes.add(solicitud);
        return solicitud;
    }

    @Override
    public Optional<Solicitud> findById(Long id) {
        
        return this.solicitudes.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Solicitud> findAll() {

        return this.solicitudes.stream().collect(Collectors.toList());
        
    }

    @Override
    public List<Solicitud> findByEstadoAndPrioridad(String estado, String prioridad) {
        return this.solicitudes.stream()
                .filter(s -> estado == null || s.getEstado().equals(estado))
                .filter(s -> prioridad == null || s.getPrioridad().equals(prioridad))
                .collect(Collectors.toList());  

    }
}
