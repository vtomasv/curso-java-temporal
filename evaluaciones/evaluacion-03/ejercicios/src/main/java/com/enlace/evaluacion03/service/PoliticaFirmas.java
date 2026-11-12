package com.enlace.evaluacion03.service;

import com.enlace.evaluacion03.config.Decisiones;
import com.enlace.evaluacion03.domain.Confidencialidad;
import com.enlace.evaluacion03.domain.FirmaOrden;
import com.enlace.evaluacion03.domain.Orden;
import com.enlace.evaluacion03.domain.TipoOrden;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class PoliticaFirmas {

    public int firmasRequeridas(Orden orden) {
        return switch (Decisiones.FIRMAS) {
            case PENDIENTE -> umbralConservador(orden);
            case ROLES_FIJOS -> firmasPorRoles(orden);
            case UMBRAL_CONFIGURABLE -> firmasPorUmbral(orden);
        };
    }

    public boolean rolPuedeFirmar(Orden orden, String rol) {
        if (Decisiones.FIRMAS == Decisiones.Firmas.PENDIENTE) {
            return orden.getConfidencialidad() == Confidencialidad.MAXIMA
                    ? "COMANDANTE".equals(rol)
                    : Set.of("OFICIAL", "COMANDANTE").contains(rol);
        }
        return switch (Decisiones.FIRMAS) {
            case PENDIENTE -> false;
            case ROLES_FIJOS -> rolPermitidoPorRoles(orden, rol);
            case UMBRAL_CONFIGURABLE -> rolPermitidoPorUmbral(orden, rol);
        };
    }

    public boolean requisitoCumplido(Orden orden, List<FirmaOrden> firmas) {
        return switch (Decisiones.FIRMAS) {
            case PENDIENTE -> firmas.size() >= umbralConservador(orden);
            case ROLES_FIJOS -> rolesObligatoriosCompletos(orden, firmas);
            case UMBRAL_CONFIGURABLE -> firmas.stream()
                    .map(FirmaOrden::getFirmante)
                    .distinct()
                    .count() >= firmasPorUmbral(orden);
        };
    }

    private int umbralConservador(Orden orden) {
        if (orden.getConfidencialidad() == Confidencialidad.MAXIMA) return 2;
        return orden.getTipo() == TipoOrden.ORDEN ? 2 : 1;
    }

    private int firmasPorRoles(Orden orden) {
        // TODO(EV03-E04-A): derive el requisito desde los roles obligatorios.
        return Integer.MAX_VALUE;
    }

    private boolean rolPermitidoPorRoles(Orden orden, String rol) {
        // TODO(EV03-E04-A): habilite sólo roles pertenecientes a la combinación exigida.
        return false;
    }

    private boolean rolesObligatoriosCompletos(Orden orden, List<FirmaOrden> firmas) {
        // TODO(EV03-E04-A): compruebe la combinación de roles, no sólo la cantidad.
        return false;
    }

    private int firmasPorUmbral(Orden orden) {
        // TODO(EV03-E04-B): derive el requisito desde una tabla/configuración explícita.
        return Integer.MAX_VALUE;
    }

    private boolean rolPermitidoPorUmbral(Orden orden, String rol) {
        // TODO(EV03-E04-B): use la misma tabla que define el umbral.
        return false;
    }
}
