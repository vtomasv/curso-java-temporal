package com.enlace.evaluacion03;

import com.enlace.evaluacion03.config.Decisiones;
import com.enlace.evaluacion03.service.ReautenticacionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@EnabledIfSystemProperty(named = "ev03.exercise", matches = "e03|all")
class E03ReautenticacionTest {

    @Test
    void e03_exige_elegir_el_alcance_de_la_reautenticacion() {
        assertThat(Decisiones.REAUTENTICACION)
                .as("EV03-E03: cambie REAUTENTICACION y documente consumo o expiración")
                .isNotEqualTo(Decisiones.Reautenticacion.PENDIENTE);
    }

    @Test
    void la_reautorizacion_pertenece_al_usuario_y_a_una_sola_orden() {
        requiereDecision();
        Fixture fixture = new Fixture();

        assertThat(fixture.servicio.autenticar(fixture.actual, "otro", "laboratorio",
                "ORDEN-1", fixture.session)).isFalse();
        assertThat(fixture.servicio.autenticar(fixture.actual, "oficial1", "incorrecta",
                "ORDEN-1", fixture.session)).isFalse();
        assertThat(fixture.servicio.autenticar(fixture.actual, "oficial1", "laboratorio",
                "ORDEN-1", fixture.session)).isTrue();

        assertThat(fixture.servicio.consumir(fixture.actual, "ORDEN-2", fixture.session))
                .as("otra orden no puede reutilizar la autorización")
                .isFalse();
        assertThat(fixture.servicio.consumir(fixture.actual, "ORDEN-1", fixture.session)).isTrue();

        boolean segundoUso = fixture.servicio.consumir(fixture.actual, "ORDEN-1", fixture.session);
        if (Decisiones.REAUTENTICACION == Decisiones.Reautenticacion.POR_CADA_LECTURA) {
            assertThat(segundoUso).as("el permiso de una lectura se consume").isFalse();
        } else {
            assertThat(segundoUso).as("la ventana breve permite reutilizar el par usuario-orden").isTrue();
        }
    }

    private void requiereDecision() {
        assumeFalse(Decisiones.REAUTENTICACION == Decisiones.Reautenticacion.PENDIENTE,
                "Primero complete DECISION(EV03-E03)");
    }

    private static final class Fixture {
        private final PasswordEncoder encoder = new BCryptPasswordEncoder();
        private final ReautenticacionService servicio = new ReautenticacionService(
                new InMemoryUserDetailsManager(User.withUsername("oficial1")
                        .password(encoder.encode("laboratorio")).roles("OFICIAL").build()), encoder);
        private final Authentication actual = new TestingAuthenticationToken(
                "oficial1", "n/a", "ROLE_OFICIAL");
        private final MockHttpSession session = new MockHttpSession();
    }
}
