package com.sigeo.clase05;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    
    private SolicitudService solicitudService;


    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;   
    }

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

    @GetMapping("/listarSolicitudes")
    public ResponseEntity<List<SolicitudResponseDto>> listarSolicitudes(
            @RequestParam(required = true) String estado,
            @RequestParam(required = false) String prioridad) 
    {
        List<Solicitud> solicitudes = solicitudService.buscarSolicitudes(estado, prioridad);
        List<SolicitudResponseDto> responseDtos = new ArrayList<>();
        for (Solicitud solicitud : solicitudes) {
            SolicitudResponseDto dto = new SolicitudResponseDto(
                solicitud.getId(),
                solicitud.getTitulo(),
                solicitud.getDescripcion(),
                solicitud.getEstado(),
                solicitud.getPrioridad()
            );
            responseDtos.add(dto);
        }

        return ResponseEntity.ok(responseDtos);
    }
}
