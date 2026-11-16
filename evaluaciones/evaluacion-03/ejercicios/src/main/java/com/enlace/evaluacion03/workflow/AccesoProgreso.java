package com.enlace.evaluacion03.workflow;

import java.util.Set;

/**
 * Evidencia no sensible de accesos que ya fueron autorizados por la capa web.
 * Nunca contiene contraseñas ni el cuerpo de una orden.
 */
public record AccesoProgreso(int accesosAutorizados, int reautenticaciones,
                             Set<String> usuarios) {
    public AccesoProgreso {
        if (accesosAutorizados < 0 || reautenticaciones < 0
                || reautenticaciones > accesosAutorizados) {
            throw new IllegalArgumentException("contadores de acceso");
        }
        if (usuarios == null) throw new IllegalArgumentException("usuarios");
        usuarios = Set.copyOf(usuarios);
    }

    public static AccesoProgreso vacio() {
        return new AccesoProgreso(0, 0, Set.of());
    }
}
