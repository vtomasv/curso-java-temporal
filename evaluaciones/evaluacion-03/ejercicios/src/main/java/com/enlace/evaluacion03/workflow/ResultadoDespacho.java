package com.enlace.evaluacion03.workflow;

import java.util.List;
import java.util.Set;

public record ResultadoDespacho(String ordenId, EstadoDespacho estado,
                                List<ReciboEnvio> recibos, Set<String> confirmadas) {
    public ResultadoDespacho {
        if (ordenId == null || ordenId.isBlank()) throw new IllegalArgumentException("ordenId");
        if (estado == null) throw new IllegalArgumentException("estado");
        if (recibos == null) throw new IllegalArgumentException("recibos");
        if (confirmadas == null) throw new IllegalArgumentException("confirmadas");
        ordenId = ordenId.trim();
        recibos = List.copyOf(recibos);
        confirmadas = Set.copyOf(confirmadas);
    }
}
