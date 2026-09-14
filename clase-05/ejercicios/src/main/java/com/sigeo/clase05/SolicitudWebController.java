package com.sigeo.clase05;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/web/solicitudes")
public class SolicitudWebController {

    private final SolicitudService solicitudService;

    public SolicitudWebController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("solicitudes", solicitudService.listarTodas());
        return "listado";
    }

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        model.addAttribute("solicitud", new CrearSolicitudDto("", "", ""));
        return "formulario";
    }

    @PostMapping("/nueva")
    public String guardar(@Valid @ModelAttribute("solicitud") CrearSolicitudDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return "formulario";
        }
        solicitudService.crearSolicitud(dto.titulo(), dto.descripcion(), dto.prioridad());
        return "redirect:/web/solicitudes";
    }
}
