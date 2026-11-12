package com.enlace.evaluacion03.workflow;

public record ReciboEnvio(String ordenId, String unidad, String reciboId) {
    public ReciboEnvio {
        ordenId = texto(ordenId, "ordenId");
        unidad = texto(unidad, "unidad");
        reciboId = texto(reciboId, "reciboId");
    }

    private static String texto(String valor, String campo) {
        if (valor == null || valor.isBlank()) throw new IllegalArgumentException(campo);
        return valor.trim();
    }
}
