package com.enlace.evaluacion03;

import com.enlace.evaluacion03.config.Decisiones;
import com.enlace.evaluacion03.domain.Confidencialidad;
import com.enlace.evaluacion03.domain.Orden;
import com.enlace.evaluacion03.domain.TipoOrden;
import com.enlace.evaluacion03.domain.UnidadDestino;
import com.enlace.evaluacion03.service.PoliticaAcceso;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@EnabledIfSystemProperty(named = "ev03.exercise", matches = "e02|all")
class E02AccesoConfidencialidadTest {

    private final PoliticaAcceso politica = new PoliticaAcceso();

    @Test
    void e02_exige_elegir_una_politica_de_acceso() {
        assertThat(Decisiones.ACCESO)
                .as("EV03-E02: cambie ACCESO y justifique la matriz o estrategia")
                .isNotEqualTo(Decisiones.Acceso.PENDIENTE);
    }

    @Test
    void la_matriz_publicada_se_aplica_a_todas_las_confidencialidades() {
        requiereDecision();

        Map<Confidencialidad, Set<String>> permitidos = Map.of(
                Confidencialidad.INTERNA, Set.of("OPERADOR", "OFICIAL", "COMANDANTE", "AUDITOR"),
                Confidencialidad.RESERVADA, Set.of("OFICIAL", "COMANDANTE", "AUDITOR"),
                Confidencialidad.MAXIMA, Set.of("COMANDANTE", "AUDITOR")
        );

        for (Confidencialidad nivel : Confidencialidad.values()) {
            Orden orden = orden(nivel);
            assertThat(politica.puedeLeer(null, orden))
                    .as("ningún usuario anónimo puede leer %s", nivel)
                    .isFalse();

            for (String rol : Set.of("OPERADOR", "OFICIAL", "COMANDANTE", "AUDITOR")) {
                assertThat(politica.puedeLeer(usuario(rol), orden))
                        .as("rol %s frente a confidencialidad %s", rol, nivel)
                        .isEqualTo(permitidos.get(nivel).contains(rol));
            }
        }
    }

    private void requiereDecision() {
        assumeFalse(Decisiones.ACCESO == Decisiones.Acceso.PENDIENTE,
                "Primero complete DECISION(EV03-E02)");
    }

    private Authentication usuario(String rol) {
        return new TestingAuthenticationToken(rol.toLowerCase(), "n/a", "ROLE_" + rol);
    }

    private Orden orden(Confidencialidad confidencialidad) {
        return new Orden("ORDEN-E02-" + confidencialidad, "Asunto E02",
                "Contenido reservado de prueba E02", TipoOrden.INFORMATIVA,
                confidencialidad, Set.of(UnidadDestino.UNIDAD_ANDINA), "oficial1",
                "IDEM-E02-" + confidencialidad);
    }
}
