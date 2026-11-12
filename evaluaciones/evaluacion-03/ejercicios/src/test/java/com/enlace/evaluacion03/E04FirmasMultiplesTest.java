package com.enlace.evaluacion03;

import com.enlace.evaluacion03.config.Decisiones;
import com.enlace.evaluacion03.domain.Confidencialidad;
import com.enlace.evaluacion03.domain.EstadoOrden;
import com.enlace.evaluacion03.domain.FirmaOrden;
import com.enlace.evaluacion03.domain.Orden;
import com.enlace.evaluacion03.domain.TipoOrden;
import com.enlace.evaluacion03.domain.UnidadDestino;
import com.enlace.evaluacion03.repository.FirmaOrdenRepository;
import com.enlace.evaluacion03.repository.OrdenRepository;
import com.enlace.evaluacion03.service.OrdenService;
import com.enlace.evaluacion03.service.PoliticaAcceso;
import com.enlace.evaluacion03.service.PoliticaFirmas;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@EnabledIfSystemProperty(named = "ev03.exercise", matches = "e04|all")
class E04FirmasMultiplesTest {

    @Test
    void e04_exige_elegir_una_politica_de_firmas() {
        assertThat(Decisiones.FIRMAS)
                .as("EV03-E04: cambie FIRMAS y documente roles u origen del umbral")
                .isNotEqualTo(Decisiones.Firmas.PENDIENTE);
    }

    @Test
    void una_orden_maxima_requiere_dos_personas_y_rechaza_duplicados() {
        requiereDecision();
        Orden orden = ordenMaxima();
        PoliticaFirmas politica = new PoliticaFirmas();

        assertThat(politica.firmasRequeridas(orden)).isEqualTo(2);
        assertThat(politica.rolPuedeFirmar(orden, "OPERADOR")).isFalse();
        List<String> rolesDePrueba;
        if (Decisiones.FIRMAS == Decisiones.Firmas.ROLES_FIJOS) {
            assertThat(politica.rolPuedeFirmar(orden, "OFICIAL")).isTrue();
            assertThat(politica.rolPuedeFirmar(orden, "COMANDANTE")).isTrue();
            rolesDePrueba = List.of("OFICIAL", "COMANDANTE");
        } else {
            String rolAdmitido = Stream.of("OFICIAL", "COMANDANTE", "AUDITOR")
                    .filter(rol -> politica.rolPuedeFirmar(orden, rol))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("La política no habilitó ningún rol firmante"));
            rolesDePrueba = List.of(rolAdmitido, rolAdmitido);
        }

        OrdenRepository ordenes = mock(OrdenRepository.class);
        FirmaOrdenRepository firmas = mock(FirmaOrdenRepository.class);
        Set<String> firmantes = new LinkedHashSet<>();
        List<FirmaOrden> registradas = new ArrayList<>();
        when(ordenes.findById(orden.getId())).thenReturn(Optional.of(orden));
        when(firmas.existsByOrdenIdAndFirmante(anyString(), anyString()))
                .thenAnswer(invocacion -> firmantes.contains(invocacion.getArgument(1, String.class)));
        when(firmas.saveAndFlush(any(FirmaOrden.class))).thenAnswer(invocacion -> {
            FirmaOrden firma = invocacion.getArgument(0, FirmaOrden.class);
            firmantes.add(firma.getFirmante());
            registradas.add(firma);
            return firma;
        });
        when(firmas.countByOrdenId(orden.getId())).thenAnswer(ignorada -> (long) firmantes.size());
        when(firmas.findByOrdenIdOrderByFirmadaEnAsc(orden.getId()))
                .thenAnswer(ignorada -> List.copyOf(registradas));

        OrdenService servicio = new OrdenService(ordenes, firmas, new PoliticaAcceso(), politica);
        servicio.firmar(orden.getId(), "persona-1", rolesDePrueba.get(0));
        assertThat(orden.getEstado()).isEqualTo(EstadoOrden.PENDIENTE_FIRMAS);

        assertThatThrownBy(() -> servicio.firmar(orden.getId(), "persona-1", rolesDePrueba.get(0)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ya firmó");

        servicio.firmar(orden.getId(), "persona-2", rolesDePrueba.get(1));
        assertThat(orden.getEstado()).isEqualTo(EstadoOrden.LISTA_PARA_ENVIO);
    }

    private void requiereDecision() {
        assumeFalse(Decisiones.FIRMAS == Decisiones.Firmas.PENDIENTE,
                "Primero complete DECISION(EV03-E04)");
    }

    private Orden ordenMaxima() {
        return new Orden("ORDEN-E04", "Asunto E04", "Contenido máximo para validar firmas",
                TipoOrden.ORDEN, Confidencialidad.MAXIMA,
                Set.of(UnidadDestino.UNIDAD_ANDINA), "oficial1", "IDEM-E04");
    }
}
