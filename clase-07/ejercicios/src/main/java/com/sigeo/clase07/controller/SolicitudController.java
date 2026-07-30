package com.sigeo.clase07.controller;

import com.sigeo.clase07.domain.Solicitud;
import com.sigeo.clase07.service.SolicitudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<Void> aprobar(@PathVariable Long id, @RequestParam String aprobador) {
        solicitudService.aprobarSolicitud(id, aprobador, false);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Solicitud> actualizar(@PathVariable Long id, @RequestBody Solicitud request) {
        // TODO(C07-E05): Implementar endpoint y manejar excepciones de concurrencia
        // Pista: Capturar ObjectOptimisticLockingFailureException y devolver 409 Conflict
        throw new UnsupportedOperationException("TODO C07-E05");
    }
}
