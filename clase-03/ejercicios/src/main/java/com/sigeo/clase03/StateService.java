package com.sigeo.clase03;

public class StateService {

    private State currentState = new FinalState();

    public void transitionState(String currentState, String newState) {
        try {
            this.currentState = this.currentState.performLowLevelTransition(newState);
        } catch (IllegalArgumentException causa) {
            throw new InvalidStateTransitionException("No se puede transicionar de " + currentState + " a " + newState, causa);
        }
    }

    private abstract class State
    { 

        public abstract State performLowLevelTransition(String newState);
    }

    private class FinalState extends State
    {
        @Override
        public State performLowLevelTransition(String newState) {
            throw new IllegalArgumentException("Estado final inmutable");
        }
    }

    private class ArchivedState extends State
    {
        @Override
        public State performLowLevelTransition(String newState) {
            System.out.println("Transicionando a " + newState);
            return new ArchivedState();
        }
    }
}


