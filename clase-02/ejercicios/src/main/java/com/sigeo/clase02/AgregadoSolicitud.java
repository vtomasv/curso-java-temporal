package com.sigeo.clase02;

public class AgregadoSolicitud {
    private String estadoActual = "BORRADOR";
    private int monto = 0;
    
    public AgregadoSolicitud(int monto) {
        this.monto = monto;
    }
    
    public void aprobar() {
        // TODO(C02-E07): Usar la clase interna ValidadorTransicion para validar
        // Si es válido, cambiar estado a "APROBADA"
        throw new UnsupportedOperationException("TODO C02-E07");
    }
    
    // TODO(C02-E07): Implementar clase interna privada ValidadorTransicion
    // Debe tener un método boolean esValida() que verifique:
    // 1. estadoActual es "BORRADOR" o "EN_REVISION"
    // 2. monto > 0
    // Nota: La clase interna puede acceder a los campos privados de AgregadoSolicitud
}
