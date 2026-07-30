package com.sigeo.clase13;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorrelacionActivityImpl implements CorrelacionActivity {
    private static final Logger logger = LoggerFactory.getLogger(CorrelacionActivityImpl.class);

    @Override
    public String procesarActividad(String input) {
        logger.info("Ejecutando actividad con input: {}", input);
        return "Procesado: " + input;
    }
}
