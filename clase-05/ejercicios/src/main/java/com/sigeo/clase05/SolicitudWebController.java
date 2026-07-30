package com.sigeo.clase05;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/web/solicitudes")
public class SolicitudWebController {

    // TODO(C05-E06): Inyectar SolicitudService por constructor

    @GetMapping
    public String listar(Model model) {
        // TODO(C05-E06): Obtener todas las solicitudes del servicio y agregarlas al modelo con el nombre "solicitudes"
        // TODO(C05-E06): Retornar el nombre de la vista "listado"
        throw new UnsupportedOperationException("TODO C05-E06");
    }

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        // TODO(C05-E06): Agregar un nuevo CrearSolicitudDto al modelo con el nombre "solicitud"
        // TODO(C05-E06): Retornar el nombre de la vista "formulario"
        throw new UnsupportedOperationException("TODO C05-E06");
    }

    @PostMapping("/nueva")
    public String guardar(@Valid @ModelAttribute("solicitud") CrearSolicitudDto dto, BindingResult result) {
        // TODO(C05-E06): Si hay errores de validación (result.hasErrors()), retornar "formulario"
        // TODO(C05-E06): Si no hay errores, llamar al servicio para crear la solicitud
        // TODO(C05-E06): Redirigir a "/web/solicitudes"
        throw new UnsupportedOperationException("TODO C05-E06");
    }
}
