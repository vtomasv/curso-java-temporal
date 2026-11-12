package com.enlace.evaluacion03.workflow;

import java.util.List;
import java.util.LinkedHashSet;

public record DespachoInput(String ordenId, List<String> unidades) {
    public DespachoInput {
        if (ordenId == null || ordenId.isBlank()) throw new IllegalArgumentException("ordenId");
        if (unidades == null || unidades.isEmpty()) throw new IllegalArgumentException("unidades");
        if (unidades.stream().anyMatch(unidad -> unidad == null || unidad.isBlank())) {
            throw new IllegalArgumentException("unidades");
        }
        ordenId = ordenId.trim();
        unidades = unidades.stream().map(String::trim).toList();
        if (new LinkedHashSet<>(unidades).size() != unidades.size()) {
            throw new IllegalArgumentException("unidades duplicadas");
        }
        unidades = List.copyOf(unidades);
    }
}
