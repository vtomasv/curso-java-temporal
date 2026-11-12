package com.enlace.evaluacion03.service;

import com.enlace.evaluacion03.config.Decisiones;
import com.enlace.evaluacion03.domain.Confidencialidad;
import com.enlace.evaluacion03.domain.Orden;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PoliticaAcceso {

    public boolean puedeLeer(Authentication authentication, Orden orden) {
        if (authentication == null || !authentication.isAuthenticated()) return false;

        return switch (Decisiones.ACCESO) {
            case PENDIENTE -> reglaConservadora(authentication, orden);
            case RBAC_CENTRAL -> evaluarRbacCentral(authentication, orden);
            case ESTRATEGIA_POR_CLASIFICACION -> evaluarEstrategia(authentication, orden);
        };
    }

    private boolean reglaConservadora(Authentication authentication, Orden orden) {
        return switch (orden.getConfidencialidad()) {
            case INTERNA -> true;
            case RESERVADA -> tieneRol(authentication, "OFICIAL")
                    || tieneRol(authentication, "COMANDANTE")
                    || tieneRol(authentication, "AUDITOR");
            case MAXIMA -> tieneRol(authentication, "COMANDANTE")
                    || tieneRol(authentication, "AUDITOR");
        };
    }

    private boolean evaluarRbacCentral(Authentication authentication, Orden orden) {
        // TODO(EV03-E02-A): implemente la matriz centralizada publicada.
        return false;
    }

    private boolean evaluarEstrategia(Authentication authentication, Orden orden) {
        // TODO(EV03-E02-B): delegue la decisión a una estrategia por clasificación.
        return false;
    }

    private boolean tieneRol(Authentication authentication, String rol) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> Set.of("ROLE_" + rol).contains(a.getAuthority()));
    }
}
