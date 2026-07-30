package com.sigeo.clase05;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    // TODO(C05-E03): Inyectar SolicitudService por constructor

    @PostMapping
    public ResponseEntity<SolicitudResponseDto> crearSolicitud(@Valid @RequestBody CrearSolicitudDto dto) {
        // TODO(C05-E03): Llamar al servicio para crear la solicitud
        // TODO(C05-E03): Mapear la entidad Solicitud a SolicitudResponseDto
        // TODO(C05-E03): Retornar 201 Created con el header Location apuntando a /api/solicitudes/{id}
        throw new UnsupportedOperationException("TODO C05-E03");
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponseDto> obtenerSolicitud(@PathVariable Long id) {
        // TODO(C05-E04): Llamar al servicio para obtener la solicitud por ID
        // TODO(C05-E04): Mapear a DTO y retornar 200 OK
        throw new UnsupportedOperationException("TODO C05-E04");
    }

    @GetMapping
    public ResponseEntity<List<SolicitudResponseDto>> listarSolicitudes(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String prioridad) {
        // TODO(C05-E04): Llamar al servicio para buscar solicitudes con los filtros
        // TODO(C05-E04): Mapear la lista de entidades a lista de DTOs y retornar 200 OK
        throw new UnsupportedOperationException("TODO C05-E04");
    }
}
