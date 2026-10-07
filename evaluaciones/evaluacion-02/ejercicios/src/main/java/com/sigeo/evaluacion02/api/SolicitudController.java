package com.sigeo.evaluacion02.api;

import com.sigeo.evaluacion02.service.SolicitudService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/solicitudes")
@Validated
public class SolicitudController {

    private final SolicitudService service;

    public SolicitudController(SolicitudService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SolicitudResponse> crear(
            @Valid @RequestBody CrearSolicitudRequest request,
            @RequestHeader("Idempotency-Key") @NotBlank String claveIdempotencia,
            Authentication authentication
    ) {
        // TODO(EV02-E05): Delegar usando el nombre autenticado y responder 201 + Location.
        return ResponseEntity.status(501).build();
    }

    @GetMapping("/{id}")
    public SolicitudResponse buscar(@PathVariable String id) {
        // TODO(EV02-E05): Delegar en el servicio.
        return null;
    }

    @PostMapping("/{id}/aprobacion")
    public SolicitudResponse aprobar(@PathVariable String id) {
        // TODO(EV02-E05): Delegar en el servicio.
        return null;
    }
}
