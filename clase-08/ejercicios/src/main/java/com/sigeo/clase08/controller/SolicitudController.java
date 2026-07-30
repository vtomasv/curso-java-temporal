package com.sigeo.clase08.controller;

import com.sigeo.clase08.model.Solicitud;
import com.sigeo.clase08.service.SolicitudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    // TODO(C08-E03): Configurar autorización: LECTOR consulta, OPERADOR crea y SUPERVISOR aprueba.
    @GetMapping
    public List<Solicitud> getAll() {
        throw new UnsupportedOperationException("TODO C08-E03");
    }

    @PostMapping
    public Solicitud create(@RequestBody Solicitud solicitud) {
        throw new UnsupportedOperationException("TODO C08-E03");
    }

    @PutMapping("/{id}")
    public Solicitud update(@PathVariable Long id, @RequestBody Solicitud solicitud) {
        return solicitudService.update(id, solicitud);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id) {
        throw new UnsupportedOperationException("TODO C08-E03");
    }
}
