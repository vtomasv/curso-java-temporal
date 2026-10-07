package com.sigeo.clase09;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditoriaActivityImpl implements AuditoriaActivity {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaActivityImpl.class);

    @Override
    public void registrarAuditoria(String mensaje) {
        log.info("Auditoría: {}", mensaje);
    }
}
