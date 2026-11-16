package com.enlace.evaluacion03.config;

import com.enlace.evaluacion03.domain.Confidencialidad;
import com.enlace.evaluacion03.domain.EstadoOrden;
import com.enlace.evaluacion03.domain.Orden;
import com.enlace.evaluacion03.domain.TipoOrden;
import com.enlace.evaluacion03.domain.UnidadDestino;
import com.enlace.evaluacion03.repository.OrdenRepository;
import com.enlace.evaluacion03.service.DespachoTemporalService;
import com.enlace.evaluacion03.workflow.DespachoInput;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class DatosIniciales {

    @Bean
    ApplicationRunner cargarDemostracion(OrdenRepository ordenes,
                                         DespachoTemporalService temporal) {
        return args -> {
            if (ordenes.count() == 0) {
                ordenes.save(new Orden(
                        "ORD-DEMO-01", "Prueba semanal del enlace",
                        "Confirmar operatividad del enlace antes de las 18:00 horas.",
                        TipoOrden.INFORMATIVA, Confidencialidad.INTERNA,
                        Set.of(UnidadDestino.UNIDAD_ANDINA, UnidadDestino.UNIDAD_COSTERA),
                        "operador", "DEMO-01"
                ));
                ordenes.save(new Orden(
                        "ORD-DEMO-02", "Antecedente de acceso restringido",
                        "Contenido ficticio de demostración para validar la reautenticación máxima.",
                        TipoOrden.ORDEN, Confidencialidad.MAXIMA,
                        Set.of(UnidadDestino.UNIDAD_AUSTRAL),
                        "comandante1", "DEMO-02"
                ));
            }

            ordenes.findAll().stream()
                    .filter(orden -> orden.getEstado() == EstadoOrden.PENDIENTE_FIRMAS
                            || orden.getEstado() == EstadoOrden.LISTA_PARA_ENVIO
                            || orden.getEstado() == EstadoOrden.EN_DESPACHO)
                    .forEach(orden -> temporal.iniciar(new DespachoInput(
                            orden.getId(),
                            orden.getDestinatarios().stream().map(Enum::name).toList()
                    )));
        };
    }
}
