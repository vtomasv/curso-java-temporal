package com.sigeo.clase06;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    // TODO(C06-E05): Implementar endpoint paginado
    // No exponer Page<Solicitud> directamente, mapear a un DTO (PageDTO)
    @GetMapping
    public Object listarPaginado(
            @RequestParam(defaultValue = "PENDIENTE") String estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // Limitar tamaño máximo de página a 50
        int finalSize = Math.min(size, 50);
        
        PageRequest pageRequest = PageRequest.of(page, finalSize, Sort.by("fechaCreacion").descending());
        Page<Solicitud> solicitudes = solicitudService.listarPorEstadoPaginado(estado, pageRequest);
        
        throw new UnsupportedOperationException("TODO C06-E05: Retornar un DTO que envuelva la página");
    }
}
