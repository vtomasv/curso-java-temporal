package com.sigeo.clase12;

import java.util.List;

public class SagaSteps {

    public record StepDefinition(String name, String action, String compensation) {}

    public List<StepDefinition> getSagaSteps() {
        // TODO(C12-E01): Definir pasos reserva-presupuesto-agenda-notificación y compensación de cada uno.
        // Retornar una lista de StepDefinition con los 4 pasos y sus compensaciones semánticas.
        throw new UnsupportedOperationException("TODO C12-E01");
    }
}
