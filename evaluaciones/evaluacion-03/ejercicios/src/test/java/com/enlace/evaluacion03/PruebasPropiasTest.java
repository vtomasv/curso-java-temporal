package com.enlace.evaluacion03;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import static org.assertj.core.api.Assertions.fail;

/**
 * Reemplace estas tres plantillas por pruebas activas de tres decisiones distintas.
 * El perfil ev03-all ejecuta estas pruebas como parte de la verificación final.
 */
@EnabledIfSystemProperty(named = "ev03.exercise", matches = "all")
class PruebasPropiasTest {

    @Test
    void decision_uno_caso_de_borde_observable() {
        fail("Reemplace por una prueba determinista con aserciones");
    }

    @Test
    void decision_dos_rechaza_un_riesgo_concreto() {
        fail("Reemplace por una prueba determinista con aserciones");
    }

    @Test
    void decision_tres_preserva_el_contrato_elegido() {
        fail("Reemplace por una prueba determinista con aserciones");
    }
}
