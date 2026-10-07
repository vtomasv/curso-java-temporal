package com.sigeo.clase09;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AprobacionActivityImpl implements AprobacionActivity {

    private static final Logger log = LoggerFactory.getLogger(AprobacionActivityImpl.class);

    @Override
    public void notificarResultado(String idSolicitud, String resultado) {
        log.info("Solicitud {} finalizó con resultado {}", idSolicitud, resultado);
    }
}
