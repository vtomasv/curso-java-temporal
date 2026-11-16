package com.enlace.evaluacion03.web;

import com.enlace.evaluacion03.domain.Confidencialidad;
import com.enlace.evaluacion03.domain.EstadoOrden;
import com.enlace.evaluacion03.domain.TipoOrden;
import com.enlace.evaluacion03.domain.UnidadDestino;
import com.enlace.evaluacion03.service.DespachoTemporalService;
import com.enlace.evaluacion03.service.OrdenService;
import com.enlace.evaluacion03.service.ReautenticacionService;
import io.temporal.client.WorkflowException;
import io.temporal.client.WorkflowUpdateException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;
import java.util.UUID;

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
    String inicio(Model model, Authentication authentication) {
        cargarInicio(model, authentication);
        if (!model.containsAttribute("crearOrdenRequest")) {
            model.addAttribute("crearOrdenRequest", formularioVacio());
        }
        return "index";
    }

    @PostMapping("/ordenes/nueva")
    String crear(@Valid @ModelAttribute CrearOrdenRequest crearOrdenRequest,
                 BindingResult errores, Authentication authentication, Model model) {
        if (errores.hasErrors()) {
            cargarInicio(model, authentication);
            return "index";
        }
        OrdenResumen creada = ordenes.crear(crearOrdenRequest, authentication.getName());
        temporal.iniciar(ordenes.prepararWorkflow(creada.id()));
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
        temporal.registrarAcceso(
                ordenes.prepararWorkflow(id),
                UUID.randomUUID().toString(),
                authentication.getName(),
                resumen.confidencialidad() == Confidencialidad.MAXIMA
        );
        OrdenDetalle detalle = ordenes.detalle(id, authentication);
        model.addAttribute("detalle", detalle);
        cargarIdentidad(model, authentication);
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
    String firmar(@PathVariable String id, Authentication authentication,
                  RedirectAttributes redirectAttributes) {
        ordenes.resumenAutorizado(id, authentication);
        String rol = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.substring("ROLE_".length()))
                .findFirst()
                .orElse("SIN_ROL");
        try {
            temporal.registrarFirma(
                    ordenes.prepararWorkflow(id), authentication.getName(), rol);
        } catch (WorkflowUpdateException exception) {
            redirectAttributes.addFlashAttribute("firmaRechazada",
                    "La firma no fue registrada: el usuario ya firmó, su rol no está "
                            + "autorizado o la etapa de firmas terminó.");
        } catch (WorkflowException exception) {
            throw new IllegalStateException(
                    "Temporal no pudo registrar la firma: " + exception.getMessage(), exception);
        }
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
        model.addAttribute("accessWorkflowId", temporal.accessWorkflowId(resumen.id()));
        model.addAttribute("puedeFirmar",
                resumen.estado() == EstadoOrden.PENDIENTE_FIRMAS
                        && tieneAlgunRol(authentication, "OFICIAL", "COMANDANTE"));
        model.addAttribute("puedeConfirmar", enDespacho
                && tieneAlgunRol(authentication, "OFICIAL", "COMANDANTE"));
        model.addAttribute("puedeCancelar", enDespacho
                && tieneAlgunRol(authentication, "COMANDANTE"));
        if (!tieneWorkflow(resumen.estado())) return;

        try {
            if (resumen.estado() == EstadoOrden.PENDIENTE_FIRMAS
                    || resumen.estado() == EstadoOrden.LISTA_PARA_ENVIO
                    || resumen.estado() == EstadoOrden.EN_DESPACHO) {
                temporal.iniciar(ordenes.prepararWorkflow(resumen.id()));
            }
            model.addAttribute("firmaTemporal", temporal.consultarFirmas(resumen.id()));
            model.addAttribute("accesoTemporal", temporal.consultarAccesos(resumen.id()));
            model.addAttribute("progreso", temporal.consultarProgreso(resumen.id()));
            model.addAttribute("recibos", temporal.consultarRecibos(resumen.id()));
        } catch (WorkflowException exception) {
            model.addAttribute("temporalError",
                    "El Workflow todavía no está disponible para consulta. Recargue la página.");
        }
    }

    private boolean tieneWorkflow(EstadoOrden estado) {
        return estado == EstadoOrden.PENDIENTE_FIRMAS
                || estado == EstadoOrden.LISTA_PARA_ENVIO
                || estado == EstadoOrden.EN_DESPACHO
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

    private void cargarInicio(Model model, Authentication authentication) {
        cargarFormulario(model);
        var listado = ordenes.listar();
        model.addAttribute("ordenes", listado);
        model.addAttribute("puedeCrear", tieneAlgunRol(authentication, "OPERADOR", "COMANDANTE"));
        model.addAttribute("totalOrdenes", listado.size());
        model.addAttribute("pendientesFirma", listado.stream()
                .filter(orden -> orden.estado() == EstadoOrden.PENDIENTE_FIRMAS
                        || orden.estado() == EstadoOrden.LISTA_PARA_ENVIO)
                .count());
        model.addAttribute("enDespacho", listado.stream()
                .filter(orden -> orden.estado() == EstadoOrden.EN_DESPACHO)
                .count());
        model.addAttribute("finalizadas", listado.stream()
                .filter(orden -> orden.estado() == EstadoOrden.ENTREGADA
                        || orden.estado() == EstadoOrden.CANCELADA
                        || orden.estado() == EstadoOrden.PENDIENTE_REVISION)
                .count());
        cargarIdentidad(model, authentication);
    }

    private void cargarIdentidad(Model model, Authentication authentication) {
        model.addAttribute("usuarioActual", authentication.getName());
        model.addAttribute("rolActual", authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.substring("ROLE_".length()))
                .findFirst()
                .orElse("USUARIO"));
    }

    private CrearOrdenRequest formularioVacio() {
        return new CrearOrdenRequest("", "", "", TipoOrden.INFORMATIVA,
                Confidencialidad.INTERNA, Set.of(UnidadDestino.UNIDAD_ANDINA), "");
    }
}
