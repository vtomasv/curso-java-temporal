package com.sigeo.clase08.controller;

import com.sigeo.clase08.model.Solicitud;
import com.sigeo.clase08.service.SolicitudService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {
    private final SolicitudService service;
    public SolicitudController(SolicitudService service) { this.service = service; }
    // DTO acotado: el cliente no puede cambiar id, propietario ni estado (E08).
    public record SolicitudInput(String descripcion) {
        public SolicitudInput {
            if (descripcion == null || descripcion.isBlank() || descripcion.length() > 200)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping public List<Solicitud> getAll() { return service.findAll(); }
    @PostMapping public Solicitud create(@RequestBody SolicitudInput input) { return service.create(input.descripcion()); }
    @PutMapping("/{id}") public Solicitud update(@PathVariable Long id, @RequestBody SolicitudInput input) {
        return service.update(id, input.descripcion());
    }
    @PostMapping("/{id}/approve") public ResponseEntity<Void> approve(@PathVariable Long id) {
        service.approve(id);
        return ResponseEntity.ok().build();
    }
}
