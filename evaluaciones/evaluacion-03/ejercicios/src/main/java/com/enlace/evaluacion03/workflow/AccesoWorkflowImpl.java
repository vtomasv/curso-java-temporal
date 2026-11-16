package com.enlace.evaluacion03.workflow;

import io.temporal.failure.ApplicationFailure;
import io.temporal.workflow.Workflow;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class AccesoWorkflowImpl implements AccesoWorkflow {

    private final Map<String, String> accesos = new LinkedHashMap<>();
    private final Set<String> usuariosConAcceso = new LinkedHashSet<>();
    private int reautenticaciones;

    @Override
    public void mantener(String ordenId) {
        texto(ordenId, "ordenId", 255);
        // La auditoría debe sobrevivir al resultado terminal del despacho. El
        // Worker puede detenerse; Temporal reconstruye este estado por replay.
        Workflow.await(() -> false);
    }

    @Override
    public void validarRegistrarAcceso(
            String accesoId, String usuario, boolean reautenticado) {
        accesoId = texto(accesoId, "accesoId", 120);
        usuario = texto(usuario, "usuario", 120);
        String evidencia = usuario + "|" + reautenticado;
        String anterior = accesos.get(accesoId);
        if (anterior != null && !anterior.equals(evidencia)) {
            throw new IllegalArgumentException("El accesoId ya identifica otra lectura");
        }
    }

    @Override
    public AccesoProgreso registrarAcceso(
            String accesoId, String usuario, boolean reautenticado) {
        try {
            validarRegistrarAcceso(accesoId, usuario, reautenticado);
        } catch (IllegalArgumentException exception) {
            // El validador normal rechaza antes de aceptar el Update. Esta defensa
            // evita un WorkflowTaskFailed incluso ante una invocación no estándar.
            throw ApplicationFailure.newNonRetryableFailure(
                    exception.getMessage(), "ACCESO_RECHAZADO");
        }
        accesoId = accesoId.trim();
        usuario = usuario.trim();
        String evidencia = usuario + "|" + reautenticado;
        String anterior = accesos.putIfAbsent(accesoId, evidencia);
        if (anterior == null) {
            usuariosConAcceso.add(usuario);
            if (reautenticado) reautenticaciones++;
        }
        return progreso();
    }

    @Override
    public AccesoProgreso consultarAccesos() {
        return progreso();
    }

    private AccesoProgreso progreso() {
        return new AccesoProgreso(
                accesos.size(), reautenticaciones, Set.copyOf(usuariosConAcceso));
    }

    private String texto(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank() || valor.length() > maximo) {
            throw new IllegalArgumentException(campo);
        }
        return valor.trim();
    }
}
