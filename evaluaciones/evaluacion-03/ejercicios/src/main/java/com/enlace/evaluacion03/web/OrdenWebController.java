package com.enlace.evaluacion03.web;

import com.enlace.evaluacion03.domain.Confidencialidad;
import com.enlace.evaluacion03.domain.EstadoOrden;
import com.enlace.evaluacion03.domain.TipoOrden;
import com.enlace.evaluacion03.domain.UnidadDestino;
import com.enlace.evaluacion03.service.DespachoTemporalService;
import com.enlace.evaluacion03.service.OrdenService;
import com.enlace.evaluacion03.service.ReautenticacionService;
import io.temporal.client.WorkflowException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
public class OrdenWebController {

    private final OrdenService ordenes;
    private final ReautenticacionService reautenticacion;
    private final DespachoTemporalService temporal;

    public OrdenWebController(OrdenService ordenes, ReautenticacionService reautenticacion,
                              DespachoTemporalService temporal) {
        this.ordenes = ordenes;
        this.reautenticacion = reautenticacion;
        this.temporal = temporal;
    }

    @GetMapping("/")
    String inicio(Model model) {
        cargarFormulario(model);
        model.addAttribute("ordenes", ordenes.listar());
        if (!model.containsAttribute("crearOrdenRequest")) {
            model.addAttribute("crearOrdenRequest", formularioVacio());
        }
        return "index";
    }

    @PostMapping("/ordenes/nueva")
    String crear(@Valid @ModelAttribute CrearOrdenRequest crearOrdenRequest,
                 BindingResult errores, Authentication authentication, Model model) {
        if (errores.hasErrors()) {
            cargarFormulario(model);
            model.addAttribute("ordenes", ordenes.listar());
            return "index";
        }
        OrdenResumen creada = ordenes.crear(crearOrdenRequest, authentication.getName());
        return "redirect:/ordenes/" + creada.id();
    }

    @GetMapping("/ordenes/{id}")
    String detalle(@PathVariable String id, Authentication authentication,
                   HttpSession session, Model model) {
        OrdenResumen resumen = ordenes.resumenAutorizado(id, authentication);
        if (resumen.confidencialidad() == Confidencialidad.MAXIMA
                && !reautenticacion.consumir(authentication, id, session)) {
            model.addAttribute("orden", resumen);
            model.addAttribute("usuarioActual", authentication.getName());
            return "reauth";
        }
        OrdenDetalle detalle = ordenes.detalle(id, authentication);
        model.addAttribute("detalle", detalle);
        cargarDespacho(detalle.resumen(), authentication, model);
        return "detalle";
    }

    @PostMapping("/ordenes/{id}/reauth")
    String reautenticar(@PathVariable String id,
                        @RequestParam String usuario,
                        @RequestParam String password,
                        Authentication authentication, HttpSession session, Model model) {
        if (!reautenticacion.autenticar(authentication, usuario, password, id, session)) {
            model.addAttribute("orden", ordenes.resumenAutorizado(id, authentication));
            model.addAttribute("usuarioActual", authentication.getName());
            model.addAttribute("error", "Las credenciales no corresponden al usuario autenticado");
            return "reauth";
        }
        return "redirect:/ordenes/" + id;
    }

    @PostMapping("/ordenes/{id}/firmar")
    String firmar(@PathVariable String id, Authentication authentication) {
        String rol = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.substring("ROLE_".length()))
                .findFirst()
                .orElse("SIN_ROL");
        ordenes.firmar(id, authentication.getName(), rol);
        return "redirect:/ordenes/" + id;
    }

    @PostMapping("/ordenes/{id}/despachar")
    String despachar(@PathVariable String id, Authentication authentication) {
        ordenes.resumenAutorizado(id, authentication);
        temporal.iniciar(ordenes.prepararDespacho(id));
        return "redirect:/ordenes/" + id;
    }

    @PostMapping("/ordenes/{id}/confirmar")
    String confirmar(@PathVariable String id,
                     @RequestParam UnidadDestino unidad,
                     @RequestParam String reciboId,
                     Authentication authentication) {
        ordenes.resumenAutorizado(id, authentication);
        temporal.confirmarRecepcion(id, unidad.name(), reciboId);
        return "redirect:/ordenes/" + id;
    }

    @PostMapping("/ordenes/{id}/cancelar")
    String cancelar(@PathVariable String id, @RequestParam String motivo,
                    Authentication authentication) {
        ordenes.resumenAutorizado(id, authentication);
        if (!temporal.cancelar(id, motivo)) {
            throw new IllegalStateException("El Workflow ya no admite cancelación");
        }
        return "redirect:/ordenes/" + id;
    }

    private void cargarDespacho(OrdenResumen resumen, Authentication authentication, Model model) {
        boolean enDespacho = resumen.estado() == EstadoOrden.EN_DESPACHO;
        model.addAttribute("workflowId", temporal.workflowId(resumen.id()));
        model.addAttribute("puedeDespachar",
                (resumen.estado() == EstadoOrden.LISTA_PARA_ENVIO || enDespacho)
                        && tieneAlgunRol(authentication, "OPERADOR", "COMANDANTE"));
        model.addAttribute("puedeConfirmar", enDespacho
                && tieneAlgunRol(authentication, "OFICIAL", "COMANDANTE"));
        model.addAttribute("puedeCancelar", enDespacho
                && tieneAlgunRol(authentication, "COMANDANTE"));
        if (!tieneWorkflow(resumen.estado())) return;

        try {
            model.addAttribute("progreso", temporal.consultarProgreso(resumen.id()));
            model.addAttribute("recibos", temporal.consultarRecibos(resumen.id()));
        } catch (WorkflowException exception) {
            model.addAttribute("temporalError",
                    "El Workflow todavía no está disponible para consulta. Recargue la página.");
        }
    }

    private boolean tieneWorkflow(EstadoOrden estado) {
        return estado == EstadoOrden.EN_DESPACHO
                || estado == EstadoOrden.ENTREGADA
                || estado == EstadoOrden.PENDIENTE_REVISION
                || estado == EstadoOrden.CANCELADA;
    }

    private boolean tieneAlgunRol(Authentication authentication, String... roles) {
        Set<String> permitidos = java.util.Arrays.stream(roles)
                .map(rol -> "ROLE_" + rol)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> permitidos.contains(authority.getAuthority()));
    }

    private void cargarFormulario(Model model) {
        model.addAttribute("tipos", TipoOrden.values());
        model.addAttribute("confidencialidades", Confidencialidad.values());
        model.addAttribute("unidades", UnidadDestino.values());
    }

    private CrearOrdenRequest formularioVacio() {
        return new CrearOrdenRequest("", "", "", TipoOrden.INFORMATIVA,
                Confidencialidad.INTERNA, Set.of(UnidadDestino.UNIDAD_ANDINA), "");
    }
}
