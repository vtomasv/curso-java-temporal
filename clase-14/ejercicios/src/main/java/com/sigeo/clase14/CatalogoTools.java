package com.sigeo.clase14;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.List;
import java.util.function.Function;

@Configuration
public class CatalogoTools {

    public record Recurso(String id, String nombre, String tipo, boolean disponible) {}
    public record ConsultaRecursoRequest(String tipo) {}

    /**
     * E03: Consulta de catálogo (Tool)
     * Exponer herramienta read-only para consultar recursos.
     */
    @Bean
    @Description("Consulta los recursos disponibles en el catálogo por tipo")
    public Function<ConsultaRecursoRequest, List<Recurso>> consultarRecursos() {
        // TODO(C14-E03): Implementar la función que devuelve una lista de recursos mockeada
        // Validar que el tipo no sea nulo o vacío
        return request -> {
            throw new UnsupportedOperationException("TODO C14-E03");
        };
    }
}
