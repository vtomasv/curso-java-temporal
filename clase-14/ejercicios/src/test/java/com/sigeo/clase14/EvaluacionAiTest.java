package com.sigeo.clase14;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ClasificadorService.class})
class EvaluacionAiTest {

    @Autowired
    private ClasificadorService clasificadorService;

    record CasoPrueba(String input, ClasificadorService.Categoria categoriaEsperada) {}

    @Test
    void testConjuntoDorado() {
        // TODO(C14-E07): Crear 20 casos de prueba y evaluar la exactitud del modelo
        List<CasoPrueba> conjuntoDorado = List.of(
                new CasoPrueba("Mi pantalla está rota", ClasificadorService.Categoria.SOPORTE),
                new CasoPrueba("Quiero comprar una licencia", ClasificadorService.Categoria.VENTAS)
                // Añadir más casos
        );

        int aciertos = 0;
        for (CasoPrueba caso : conjuntoDorado) {
            try {
                ClasificadorService.ClasificacionDTO resultado = clasificadorService.clasificarSolicitud(caso.input());
                if (resultado.categoria() == caso.categoriaEsperada()) {
                    aciertos++;
                }
            } catch (UnsupportedOperationException e) {
                // Ignorar para que el test falle
            }
        }

        double exactitud = (double) aciertos / conjuntoDorado.size();
        assertThat(exactitud).isGreaterThan(0.8); // Esperamos al menos 80% de exactitud
    }
}
