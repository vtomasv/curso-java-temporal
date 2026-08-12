package com.sigeo.clase02;

public class AgregadoSolicitud {
    private int monto = 0;
    private Solicitud solicitud;

    
    public AgregadoSolicitud(int monto) {
        this.monto = monto;
        this.solicitud = new Solicitud("Solicitud de monto: " + monto);
        this.solicitud.setEstado(new EnRevision());
    }
    
    public void aprobar() {
        this.solicitud.getEstado().aprobar(this.solicitud, "firma pro el monto: " + this.monto);
    }
    
}
