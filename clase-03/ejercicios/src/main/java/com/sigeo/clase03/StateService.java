package com.sigeo.clase03;

public class StateService {

    public void transitionState(String currentState, String newState) {
        try {
            performLowLevelTransition(currentState, newState);
        } catch (IllegalArgumentException e) {
            // TODO(C03-E05): Atrapar la excepción de bajo nivel y lanzar InvalidStateTransitionException.
            // El mensaje debe ser: "No se puede transicionar de " + currentState + " a " + newState
            // Se debe preservar la excepción original como causa.
            throw new UnsupportedOperationException("TODO C03-E05");
        }
    }

    private void performLowLevelTransition(String current, String next) {
        if ("FINAL".equals(current)) {
            throw new IllegalArgumentException("Estado final inmutable");
        }
    }
}
